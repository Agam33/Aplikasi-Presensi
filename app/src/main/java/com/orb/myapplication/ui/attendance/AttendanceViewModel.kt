package com.orb.myapplication.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.orb.myapplication.firebase.AttendanceService
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.model.Attendance

class AttendanceViewModel: ViewModel() {

    private val _getTimeIn: MutableLiveData<Attendance> by lazy {
        MutableLiveData<Attendance>().also {
            AttendanceService.getDbReferenceByCurrentDate().child("in").child(UserService.userId()).addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Attendance::class.java)
                    it.postValue(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    it.postValue(Attendance())
                }
            })
        }
    }

    private val _getTimeOut: MutableLiveData<Attendance> by lazy {
        MutableLiveData<Attendance>().also {
            AttendanceService.getDbReferenceByCurrentDate().child("out").child(UserService.userId()).addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Attendance::class.java)
                    it.postValue(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    it.postValue(Attendance())
                }
            })
        }
    }

    val getTimeOut: LiveData<Attendance> = _getTimeOut
    val getTimeIn: LiveData<Attendance> = _getTimeIn
}
/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */