package com.orb.myapplication.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.orb.myapplication.R
import com.orb.myapplication.model.Registration
import com.orb.myapplication.model.User
import com.orb.myapplication.ui.LoginActivity
import com.orb.myapplication.ui.home.HomeActivity

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */
object UserService {

    private const val TAG_USERS = "users"

    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database
    private val dbRef: DatabaseReference = database.getReference(TAG_USERS)

    fun createWithEmailAndPassword(
        registration: Registration,
        result: (Boolean) -> Unit = {},
    ) {
        auth.createUserWithEmailAndPassword(registration.email, registration.password)
            .addOnCompleteListener { task ->
                result(task.isSuccessful)
                if(task.isSuccessful) {
                    val data = User(
                        dbId = userId(),
                        userId = registration.userId,
                        name = registration.name,
                        email = registration.email,
                        addressName = registration.addressName,
                        phone = registration.phone,
                        jobTitle = registration.jobTitle,
                        zipCode = registration.zipCode,
                        gender = registration.gender,
                    ).toMap()
                    setUserData(data) // add data to firebase - Realtime Database
                }
            }
    }

    fun signWithEmailAndPassword(context: Context, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) {
                context.startActivity(Intent(context, HomeActivity::class.java))
                (context as Activity).finish()

                if(isVerified()) sendEmailVerification()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.wrong_password_or_email),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUserData(
        result: (User?) -> Unit
    ) {
        getDbReferenceByUserId().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(User::class.java)
                result(data)
            }

            override fun onCancelled(error: DatabaseError) {
               result(User())
            }

        })
    }

    private fun setUserData(data: Map<String, Any>) =
        dbRef.child(userId()).setValue(data)

    fun userId() = auth.currentUser?.uid ?: ""

    fun getCurrentUser() = auth.currentUser

    private fun getDbReferenceByUserId() = dbRef.child(userId())

   fun isVerified(): Boolean = auth.currentUser?.isEmailVerified ?: false

    private fun sendEmailVerification() = auth.currentUser?.sendEmailVerification()

    fun signOut() = auth.signOut()

    fun deleteAccount(
        result: () -> Unit
    ) {
        auth.currentUser?.delete()?.addOnCompleteListener {
           result()
        }
    }

    fun updateUserData(data: Map<String, Any>) =
        dbRef.child(userId()).updateChildren(data)
}

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */