package com.orb.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.model.User

class ProfileViewModel: ViewModel() {

    fun updateProfile(user: User) =
        UserService.updateUserData(user.toMap())
}