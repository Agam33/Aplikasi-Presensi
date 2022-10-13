package com.orb.myapplication.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.orb.myapplication.model.Attendance
import com.orb.myapplication.utils.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.math.absoluteValue

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
object AttendanceService {

    private const val TAG_ATTENDANCE = "attendance"
    private val database = Firebase.database
    private val dbRef: DatabaseReference = database.getReference(TAG_ATTENDANCE)

    fun inTime(userId: String) {

        val data = Attendance(
            currentTimeToString(),
            getLateTime()
        ).toMap()

        dbRef
            .child(LocalDate.now().year.toString())
            .child(LocalDate.now().monthValue.toString())
            .child(LocalDate.now().dayOfMonth.toString())
            .child("in")
            .child(userId).setValue(data)
    }

    fun outTime(userId: String) {
        val data = Attendance(
            currentTimeToString(),
            0
        ).toMap()

        dbRef
            .child(LocalDate.now().year.toString())
            .child(LocalDate.now().monthValue.toString())
            .child(LocalDate.now().dayOfMonth.toString())
            .child("out")
            .child(userId).setValue(data)
    }

    fun getDbReferenceByCurrentDate() = dbRef.child(currentDateToString())

    fun getInTime(
        userId: String,
        result: (Attendance?) -> Unit
    ) {
        dbRef
            .child(LocalDate.now().year.toString())
            .child(LocalDate.now().monthValue.toString())
            .child(LocalDate.now().dayOfMonth.toString())
            .child("in")
            .child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Attendance::class.java)
                    result(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    result(Attendance())
                }

            })
    }

    fun getOutTime(
        userId: String,
        result: (Attendance?) -> Unit
    ) {
        dbRef
            .child(LocalDate.now().year.toString())
            .child(LocalDate.now().monthValue.toString())
            .child(LocalDate.now().dayOfMonth.toString())
            .child("out")
            .child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Attendance::class.java)
                    result(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    result(Attendance())
                }

            })
    }

}
