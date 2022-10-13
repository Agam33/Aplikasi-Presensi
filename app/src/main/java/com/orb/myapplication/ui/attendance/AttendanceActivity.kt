package com.orb.myapplication.ui.attendance

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.orb.myapplication.R
import com.orb.myapplication.abs.AbsLocation
import com.orb.myapplication.databinding.ActivityAttendanceBinding
import com.orb.myapplication.firebase.AttendanceService
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.utils.*
import com.orb.myapplication.utils.Haversine.toMeters
import java.time.LocalDateTime
import java.util.concurrent.Executor

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
class AttendanceActivity : AbsLocation() {

    private lateinit var binding: ActivityAttendanceBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPromptInfo: BiometricPrompt.PromptInfo

    private lateinit var attendanceViewModel: AttendanceViewModel

    /*
        @Param location : retrieve data from Activity AbsLocation()

     */
    override fun updateLocation(location: Location?) {
        location?.let {
            val currLat = location.latitude
            val currLon = location.longitude
            val status = isDistanceAllowedToAttendance(currLat, currLon)
            binding.btnAttendance.isEnabled = status
        } ?: Toast.makeText(this@AttendanceActivity,
            getString(R.string.txt_requires_location),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        handleButton()
        observer()
    }

    private fun setupActionBar() {
        supportActionBar?.title = ""
        supportActionBar?.elevation = 0f
    }

    private fun observer() {
        attendanceViewModel = ViewModelProvider(this@AttendanceActivity)[AttendanceViewModel::class.java]
    }

    private fun handleButton() = with(binding) {
        btnAttendance.setOnClickListener {
            checkBiometricAvailable()
        }
    }

    private fun checkBiometricAvailable() {
        val biometricManager = BiometricManager.from(this@AttendanceActivity)
        when(biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> biometricSuccess()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> biometricEnrolled()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> biometricErrorNoHardware()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> biometricErrorNoHardware()
        }
    }

    // Pesan tidak ada sensor sidik jari
    private fun biometricErrorNoHardware() =e
        Toast.makeText(
            this@AttendanceActivity,
            getString(R.string.txt_no_biometric_hardware), Toast.LENGTH_SHORT).show()

    private fun changeAnimationView(state: Boolean) = with(binding) {
        animationView.setAnimation(
            if(state) getString(R.string.txt_anim_972_done)
            else getString(R.string.txt_anim_radar_searching))
        animationView.playAnimation()
    }

    // setelah melakukan verifikasi sidik jari
    private fun biometricSuccess() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.txt_verification))
            .setSubtitle(getString(R.string.txt_use_biometric))
            .setNegativeButtonText(getString(R.string.txt_cancel))
            .build()
        biometricPrompt = BiometricPrompt(this , executor,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    changeAnimationView(true)
                    timeToAttendance()
                }
            })
        biometricPrompt.authenticate(biometricPromptInfo)
    }

    // mengambil fitur biometric
    private fun biometricEnrolled() =
        AlertDialog.Builder(this@AttendanceActivity)
            .setTitle(getString(R.string.txt_title_enrolled_biometric))
            .setMessage(getString(R.string.txt_enrolled_biometric_msg))
            .setNegativeButton(getString(R.string.txt_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.txt_active_biometric)) { _, _ ->
                startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            }.create().show()

    // masukan data absen ke dalam firebase
    private fun timeToAttendance() {
        UserService.getUserData {
            when (LocalDateTime.now().hour) {
                in ATTENDANCE_START_AT -> AttendanceService.inTime(it?.userId ?: "")
                in ATTENDANCE_END_AT -> AttendanceService.outTime(it?.userId ?: "")
                else -> Toast.makeText(this@AttendanceActivity, getString(R.string.txt_not_allowed_attendance), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isTimeAllowedToAttendance(): Boolean =
        when (LocalDateTime.now().hour) {
            in ATTENDANCE_START_AT -> true
            in ATTENDANCE_END_AT -> true
            else -> false
        }

    private fun isDistanceAllowedToAttendance(lat: Double, lon: Double): Boolean {
        val distance = Haversine.getDistance(OFFICE_LATITUDE, OFFICE_LONGITUDE, lat, lon)
            .toMeters()
            .toInt()
        return distance < 35
    }
}

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */