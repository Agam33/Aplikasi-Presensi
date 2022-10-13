package com.orb.myapplication.model

data class Attendance(
    var time: String = "",
    var lateTime: Int = 0
) {

    fun toMap(): Map<String, Any> =
        mapOf(
            "time" to time,
            "lateTime" to lateTime
        )
}

