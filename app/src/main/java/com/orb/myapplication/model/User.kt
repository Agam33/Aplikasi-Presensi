package com.orb.myapplication.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var dbId: String = "",
    override var name: String = "",
    override var email: String = "",
    var imgUrl: String = "",
    override var userId: String = "",
    override var addressName: String = "",
    override var phone: String = "",
    override var jobTitle: String = "",
    override var zipCode: Int = 0,
    override var gender: String = "",
): Address, Employee {

    constructor(
         name: String,
         email: String,
         userId: String,
         imgUrl: String,
         addressName: String,
         phone: String,
         jobTitle: String,
         gender: String,
         zipCode: Int
     ): this (
         name = name,
         email = email,
         imgUrl = imgUrl,
         userId = userId,
         addressName = addressName,
         phone = phone,
         jobTitle = jobTitle,
         gender = gender,
     )

    @Exclude
    fun toMap(): Map<String, Any> =
        mapOf(
            "dbId" to dbId,
            "userId" to userId,
            "name" to name,
            "email" to email,
            "addressName" to addressName,
            "jobTitle" to jobTitle,
            "phone" to phone,
            "imgUrl" to imgUrl,
//            "zipCode" to zipCode,
            "gender" to gender
        )
}

interface Address {
    var addressName: String
    var zipCode: Int
}

interface Employee {
    var userId: String
    var jobTitle: String
    var phone: String
    var email: String
    var name: String
    var gender: String
}



