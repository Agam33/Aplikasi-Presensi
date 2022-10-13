package com.orb.myapplication.utils

import kotlin.math.cos
import kotlin.math.asin
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.sin

/*
    src : https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
 */

object Haversine {

    fun getDistance(lt1: Double, ln1: Double, lt2: Double, ln2: Double): Double {

        // distance between latitudes and longitudes
        val dLat = Math.toRadians(lt2 - lt1)
        val dLon = Math.toRadians(ln2 - ln1)

        // convert to radians
        val lat1ToRadians = Math.toRadians(lt1)
        val lat2ToRadians = Math.toRadians(lt2)

        // apply formula
        val a = sin(dLat / 2).pow(2) +
                sin(dLon / 2).pow(2) *
                cos(lat1ToRadians) * cos(lat2ToRadians)
        val rad = 6371
        val c = 2 * asin(sqrt(a))
        return rad * c
    }

    fun Double.toMeters() = this * 1000
}