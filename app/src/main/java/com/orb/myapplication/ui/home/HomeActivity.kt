package com.orb.myapplication.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.orb.myapplication.R
import com.orb.myapplication.abs.AbsLocation
import com.orb.myapplication.databinding.ActivityHomeBinding
import com.orb.myapplication.firebase.AttendanceService
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.ui.LoginActivity
import com.orb.myapplication.ui.attendance.AttendanceActivity
import com.orb.myapplication.ui.profile.ProfileActivity
import java.time.LocalDateTime

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
class HomeActivity: AbsLocation() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var homeViewModel: HomeViewModel

    override fun updateLocation(location: Location?) {
        location?.let {
            val latitude = location.latitude
            val longitude = location.longitude
            homeViewModel.currentLocation(latitude, longitude)
        } ?:  Toast.makeText(this@HomeActivity, getString(R.string.txt_requires_location), Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvGreeting.text = greetingMessage()
        buttonSetup()
        observer()
        attendanceService()
        playAnimation()
    }

    private fun buttonSetup() = with(binding) {
        cvAccount.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
        }

        cvAttendance.setOnClickListener {
            if(UserService.isVerified()) {
                startActivity(Intent(this@HomeActivity, AttendanceActivity::class.java))
            } else {
                Toast.makeText(
                    this@HomeActivity,
                    getString(R.string.txt_email_is_not_verified),
                    Toast.LENGTH_LONG).show()
            }
            startActivity(Intent(this@HomeActivity, AttendanceActivity::class.java))
        }

        cvLogout.setOnClickListener {
            UserService.signOut()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun observer() = with(binding) {
        homeViewModel = ViewModelProvider(this@HomeActivity)[HomeViewModel::class.java]

        homeViewModel.distanceInMeters.observe(this@HomeActivity) {
            tvDistance.text = String.format(getString(R.string.txt_distance), it)
        }
    }

    private fun attendanceService() = with(binding) {
        UserService.getUserData {
            AttendanceService.getInTime(it?.userId ?: "") {
                tvStatusIn.text = String.format(getString(R.string.txt_status_in), it?.time ?: "-")
            }

            AttendanceService.getOutTime(it?.userId ?: "") {
                tvStatusOut.text = String.format(getString(R.string.txt_status_out), it?.time ?: "-")
            }
        }
    }

    private fun playAnimation() = with(binding) {
        val animAccFrame = ObjectAnimator.ofFloat(cvAccount, View.ALPHA, 1f).setDuration(650)
        val animAttendanceFrame = ObjectAnimator.ofFloat(cvAttendance, View.ALPHA, 1f).setDuration(650)
        val animLogoutFrame = ObjectAnimator.ofFloat(cvLogout, View.ALPHA, 1f).setDuration(650)

        AnimatorSet().apply {
            playSequentially(animAccFrame, animAttendanceFrame, animLogoutFrame)
            start()
        }
    }

    private fun greetingMessage(): String {
        val currentTime = LocalDateTime.now()
        return when (currentTime.hour) {
            in 4..9 -> getString(R.string.txt_pagi)
            in 10..13 -> getString(R.string.txt_siang)
            in 14..17 -> getString(R.string.txt_sore)
            else -> getString(R.string.txt_malam)
        }
    }
}
