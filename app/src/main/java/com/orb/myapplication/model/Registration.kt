package com.orb.myapplication.model

data class Registration(
    override var userId: String,
    override var name: String,
    override var email: String,
    var password: String,
    override var phone: String,
    override var jobTitle: String,
    override var addressName: String,
    override var zipCode: Int,
    override var gender: String,
): Employee, Address
