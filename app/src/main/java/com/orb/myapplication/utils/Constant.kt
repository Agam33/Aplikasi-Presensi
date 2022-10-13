package com.orb.myapplication.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

const val OFFICE_LATITUDE = -6.218501052944883
const val OFFICE_LONGITUDE = 106.82493119543987
const val PERMISSION_FINE_LOCATION = 99
val ATTENDANCE_START_AT = 5..15
val ATTENDANCE_END_AT = 16..23

private val MAIN_THREAD = Handler(Looper.getMainLooper())

fun mainThreadDelay(t: () -> Unit) =
    MAIN_THREAD.postDelayed(t, 3000)

fun isValidEmail(email: CharSequence): Boolean =
     android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

@SuppressLint("SimpleDateFormat")
fun currentDateToString(): String =
    SimpleDateFormat("ddMMyyyy").format(Date())

fun currentTimeToString(): String {
    val localDateTime = LocalDateTime.now()
    val hours = if(localDateTime.hour <= 9) "0${localDateTime.hour}" else "${localDateTime.hour}"
    val minutes = if(localDateTime.minute <= 9) "0${localDateTime.minute}" else "${localDateTime.minute}"
    val seconds = if(localDateTime.second <= 9) "0${localDateTime.second}" else "${localDateTime.second}"
    return StringBuilder().apply {
        append(hours)
        append(":")
        append(minutes)
        append(":")
        append(seconds)
    }.toString()
}


fun getLateTime(): Int {
    val inTime = 9
    val currentHour = LocalDateTime.now().hour
    return currentHour - inTime
}


/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
