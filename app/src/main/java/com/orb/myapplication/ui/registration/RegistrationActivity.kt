package com.orb.myapplication.ui.registration

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.orb.myapplication.R
import com.orb.myapplication.databinding.ActivityRegistrationBinding
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.model.Registration
import com.orb.myapplication.ui.LoginActivity
import com.orb.myapplication.utils.isValidEmail
/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.txt_registration)

        binding.btnSignUp.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() = with(binding) {
        val userId = edtId.text.toString().trim()
        val name = edtName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()
        val phone = edtPhone.text.toString().trim()
        val zipCode = edtZipCode.text.toString().trim()
        val address = edtAddress.text.toString().trim()
        val jobTitle = edtJobTitle.text.toString().trim()

        val gender: String
        val selectedGender = rdGroupGender.checkedRadioButtonId
        gender = if(selectedGender == rdBtnPria.id) rdBtnPria.text.toString()
        else rdBtnWanita.text.toString()

        if(zipCode == "") {
            edtZipCode.error = getString(R.string.empty_column)
            return
        }

        if(phone == "") {
            edtPhone.error = getString(R.string.empty_column)
            return
        }

        if(address == "") {
            edtAddress.error = getString(R.string.empty_column)
            return
        }

        if(jobTitle == "") {
            edtJobTitle.error = getString(R.string.empty_column)
            return
        }

        if(userId == "") {
            edtId.error = getString(R.string.empty_column)
            return
        }

        if(name == "" ) {
            edtName.error = getString(R.string.empty_column)
            return
        }

        if(email == "") {
            edtEmail.error = getString(R.string.empty_column)
            return
        }

        if(password == "") {
            edtPassword.error = getString(R.string.empty_column)
            return
        }

        if(confirmPassword == "") {
            edtConfirmPassword.error = getString(R.string.empty_column)
            return
        }

        if(!isValidEmail(email)) {
            edtEmail.error = getString(R.string.email_pattern)
            return
        }

        if(password.length < 8) {
            edtPassword.error = getString(R.string.password_length)
            return
        }

        if(confirmPassword != password) {
            edtConfirmPassword.error = getString(R.string.confirm_password_not_equals_with_password)
            return
        }

        val registration = Registration(
            userId = userId,
            name = name,
            email = email,
            password = password,
            phone = phone,
            jobTitle = jobTitle,
            addressName = address,
            zipCode = zipCode.toInt(),
            gender = gender
        )

        UserService.createWithEmailAndPassword(registration) { isSuccessful ->
            if(isSuccessful) {
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this@RegistrationActivity,
                    getString(R.string.email_already_exist),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

