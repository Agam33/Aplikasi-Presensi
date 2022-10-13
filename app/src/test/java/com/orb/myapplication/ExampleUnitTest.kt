package com.orb.myapplication

import android.annotation.SuppressLint
import android.text.format.Time
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addition_isCorrect() {
        val locale = Locale("in", "id")
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", locale)
        val inTime = simpleDateFormat.parse("09:00:00").time
        val lateTime = simpleDateFormat.parse("13:01:60").time
        val x = (inTime - lateTime).absoluteValue
        val currentTimeInMillis = 0L

        val hours = TimeUnit.MILLISECONDS.toHours(currentTimeInMillis) % 24
        val minute = TimeUnit.MILLISECONDS.toMinutes(currentTimeInMillis) % 60
        val second = (currentTimeInMillis / 1000) % 60
        println("intTime: $inTime\n lateTime: $lateTime\n Hours: $hours\n Minute: $minute\n Second: $second")
        println(x)
    }

    @Test
    fun testCurrentTimeInMillis() {
//        println(timeMillisToString(LocalTime.now().toNanoOfDay()))
        val inTime = "09:00:00"
        val currentTime = LocalTime.now().toNanoOfDay()
        val lateTime = (timeFormatToMillis(inTime)) - currentTime

        val hour = TimeUnit.MILLISECONDS.toHours(lateTime.absoluteValue) % 24
        val minute = TimeUnit.MILLISECONDS.toMinutes(lateTime.absoluteValue) % 60

        val inTimeToMillis = timeFormatToMillis(inTime)

        println("inTimeToMillis: $inTimeToMillis")

        println(
            timeMillisToString(inTimeToMillis)
        )
    }

    @Test
    fun testLocalDateYYYYmmdd() {
        val day = LocalDate.now().dayOfMonth
        val month = LocalDate.now().monthValue
        val year = LocalDate.now().year

        println("Year: $year\n Month: $month\n Day: $day")
    }

    private fun timeMillisToString(millis: Long): String {
        val h = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val m = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val s = (millis / 1000) % 60
        return "${
            if(h <= 9) "0$h" else "$h"}:${
            if(m <= 9) "0$m" else "$m"}:${
            if(s <= 9) "0$s" else "$s"}"
    }

    @SuppressLint("SimpleDateFormat")
   private fun timeFormatToMillis(time: String): Long {
        val locale = Locale("in", "ID")
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", locale)
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        return simpleDateFormat.parse(time).time
    }
}