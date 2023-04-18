package com.example.bottomnavigation.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    binding.etEmail.text?.clear()
                    Toast.makeText(this,"Please check your mail and reset the password",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,SIgnInActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                }
        }

        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this,SIgnInActivity::class.java))
            finish()
        }

    }
}