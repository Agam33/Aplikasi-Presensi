package com.orb.myapplication.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.orb.myapplication.R
import com.orb.myapplication.databinding.ActivityProfileBinding
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.model.User
import com.orb.myapplication.utils.hideView

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        observer()
        userService()
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0f
    }

    private fun observer() {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private fun userService() {
        UserService.getUserData {
            setupUserProfile(it ?: User())
        }
    }

    private fun updateProfile(user: User)  {
        profileViewModel.updateProfile(user).addOnCompleteListener {
            Toast.makeText(
                this@ProfileActivity,
                getString(R.string.txt_update_profile_success),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUserProfile(user: User) = with(binding) {
        tvId.text = String.format(getString(R.string.user_id), user.userId)
        edtName.hint = user.name
        edtEmail.hint = user.email
        edtAddress.hint = user.addressName
        edtPhone.hint = user.phone
        edtJobTitle.hint = user.jobTitle
        edtGender.hint = user.gender

       btnUpdate.setOnClickListener {
            val name = if(edtName.text.toString() == "") edtName.hint.toString()
            else edtName.text.toString().trim()

           val address = if(edtAddress.text.toString() == "") edtAddress.hint.toString()
           else edtAddress.text.toString().trim()

            updateProfile(User(
                dbId = user.dbId,
                userId = user.userId,
                name = name,
                email = user.email,
                imgUrl = user.imgUrl,
                addressName = address,
                gender = user.gender,
                jobTitle = user.jobTitle,
                phone = user.phone,
                zipCode = user.zipCode,
            ))
        }

        if(UserService.isVerified()) tvEmailStatus.hideView(true)
    }
}

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */