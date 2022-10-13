package com.orb.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.orb.myapplication.firebase.AttendanceService
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.model.Attendance
import com.orb.myapplication.utils.Haversine
import com.orb.myapplication.utils.Haversine.toMeters
import com.orb.myapplication.utils.OFFICE_LATITUDE
import com.orb.myapplication.utils.OFFICE_LONGITUDE

class HomeViewModel: ViewModel() {

    private val _distance = MutableLiveData<Int>()

    val distanceInMeters: LiveData<Int> = _distance

    fun currentLocation(lat: Double, lon: Double) {
        val distance = Haversine
            .getDistance(OFFICE_LATITUDE, OFFICE_LONGITUDE, lat, lon)
            .toMeters()
            .toInt()
        _distance.postValue(distance)
    }
}