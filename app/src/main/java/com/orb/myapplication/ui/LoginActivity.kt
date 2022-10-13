package com.orb.myapplication.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.orb.myapplication.R
import com.orb.myapplication.databinding.ActivityLoginBinding
import com.orb.myapplication.firebase.UserService
import com.orb.myapplication.ui.registration.RegistrationActivity
/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        buttonSetup()
        playAnimation()
    }

    private fun buttonSetup() = with(binding) {
        btnRegis.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        btnLogin.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() = with(binding) {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if(email == "") {
            edtEmail.error = getString(R.string.empty_column)
            return
        }

        if(password == "") {
            edtPassword.error = getString(R.string.empty_column)
            return
        }

        UserService.signWithEmailAndPassword(this@LoginActivity, email, password)
    }

    private fun playAnimation() = with(binding) {
        val animFrame = ObjectAnimator.ofFloat(cvInputContainer, View.ALPHA, 1f).setDuration(750)
        val animLoginBtn = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(850)
        val animRegisBtn = ObjectAnimator.ofFloat(btnRegis, View.ALPHA, 1f).setDuration(850)

        AnimatorSet().apply {
            playSequentially(animFrame, animLoginBtn, animRegisBtn)
            start()
        }

    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}

