package com.example.mycookingapp.favourites_screen

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycookingapp.R
import com.example.mycookingapp.data.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var textEmailField: TextInputEditText
    private lateinit var textPasswordField: TextInputEditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        auth = FirebaseAuth.getInstance()
        textEmailField = findViewById(R.id.email_field)
        textPasswordField = findViewById(R.id.password_field)
    }

    fun login_user(view: View) {
        var email = textEmailField.text.toString()
        var password = textPasswordField.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        Toast.makeText(
                            this, "email не существует", Toast.LENGTH_SHORT,
                        ).show()
                    } else if (errorCode == "ERROR_WRONG_PASSWORD") {
                        Toast.makeText(
                            this, "неверный пароль", Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        Toast.makeText(this, "ошибка.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
    }

    fun register_user(view: View) {
        var email = textEmailField.text.toString()
        var password = textPasswordField.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val currentUser = auth.currentUser
                    val userId = currentUser!!.uid
                    val user = User(mutableListOf())

                    val database =
                        FirebaseDatabase.getInstance().getReference("users")

                    database.child(userId).setValue(user).addOnSuccessListener {
                        Toast.makeText(this, "Аккаунт создан", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "ошибка: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    val errorCode = (task.exception as FirebaseAuthException).errorCode

                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                        Toast.makeText(
                            this, "email уже существует", Toast.LENGTH_SHORT,
                        ).show()

                    } else if (errorCode == "ERROR_INVALID_EMAIL") {
                        Toast.makeText(
                            this, "неверный формат email", Toast.LENGTH_SHORT,
                        ).show()
                    } else if (errorCode == "ERROR_WEAK_PASSWORD") {
                        Toast.makeText(
                            this, "пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                }
            }
    }

    fun back(view: View) {
        finish()
    }

}