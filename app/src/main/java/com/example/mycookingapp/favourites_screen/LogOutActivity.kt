package com.example.mycookingapp.favourites_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.mycookingapp.R
import com.example.mycookingapp.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth

class LogOutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val emailField: TextView = findViewById(R.id.user_details)
        emailField.setText(user?.email)
    }

    fun logOut(view: View){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(applicationContext, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun back(view: View) {
        finish()
    }
}