package com.example.bottomnavigation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.ActivityMainBinding
import com.example.bottomnavigation.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener { createNewUser() }
        binding.tvSignIn.setOnClickListener { goingToSignInActivity() }


    }

    private fun goingToSignInActivity() {

       
        val email = binding.etEmail.text
        val password = binding.etPassword
        val confirmPassword = binding.etConfirmPassword



        val intent = Intent(this, SIgnInActivity::class.java)
        startActivity(intent)
    }

    private fun createNewUser() {



        val intent = Intent(this,SIgnInActivity::class.java)
        startActivity(intent)
    }
}