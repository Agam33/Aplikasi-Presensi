package com.orb.myapplication.abs

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.orb.myapplication.R
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.utils.PERMISSION_FINE_LOCATION
import com.orb.myapplication.utils.mainThreadDelay

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
abstract class AbsLocation: AppCompatActivity() {

    // google API for location service
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // for settings related to fusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    abstract fun updateLocation(location: Location?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initLocationRequest()
        initLocationCallback()
        startLocationUpdate()
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 1000
        }
    }

    private fun initLocationCallback() {
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                updateLocation(locationResult.lastLocation)
                isLocationMocked(locationResult.lastLocation)
            }
        }
    }

    private fun startLocationUpdate() {
        checkLocationPermission()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        updateGPS()
    }

    private fun updateGPS() {
        checkLocationPermission()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                updateLocation(location)
                isLocationMocked(location)
            } ?: Toast.makeText(this@AbsLocation, getString(R.string.txt_requires_location), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            when(requestCode) {
                PERMISSION_FINE_LOCATION -> {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        updateGPS()
                    } else {
                        Toast.makeText(this@AbsLocation, getString(R.string.txt_requires_location_permission), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) { }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this@AbsLocation,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@AbsLocation,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_FINE_LOCATION )
            return
        }
    }


    private fun isLocationMocked(location: Location) {
        val isMocked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            location.isMock
        } else {
            @Suppress("DEPRECATION")
            location.isFromMockProvider
        }

        if(isMocked) {

            AlertDialog.Builder(this@AbsLocation)
                .setIcon(R.drawable.ic_baseline_report_problem_24)
                .setTitle(getString(R.string.txt_warning))
                .setMessage(getString(R.string.txt_warning_mock_location))
                .create()
                .show()

            mainThreadDelay {
                UserService.signOut()
                finish()
            }
        }
    }
}

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */