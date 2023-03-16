package com.example.bottomnavigation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation.databinding.ActivitySignInBinding


class SIgnInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var sharedPreferences: SharedPreferences
    private  var userPreferences: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userPreferences = sharedPreferences.getString("user_preference", "")



        binding.btnLogin.setOnClickListener { loggingUser() }
        binding.tvSignUp.setOnClickListener { goingToSignUpActivity() }


    }

    private fun goingToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun loggingUser() {

        if(userPreferences.equals("Client")){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else if(userPreferences.equals("Contractor")){
            val intent = Intent(this, ContractorMainActivity::class.java)
            startActivity(intent)
        }


    }
}