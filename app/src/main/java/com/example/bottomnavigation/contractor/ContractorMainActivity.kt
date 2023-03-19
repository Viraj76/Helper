package com.example.bottomnavigation.contractor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.ActivityContractorMainBinding

class ContractorMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContractorMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = Navigation.findNavController(this,R.id.hostFragment)
        NavigationUI.setupWithNavController(binding.btmNavCon,navController)
    }
}