package com.example.bottomnavigation.contractor.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.bottomnavigation.R

import com.example.bottomnavigation.databinding.ActivityContractorMainBinding


class ContractorMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContractorMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = Navigation.findNavController(this, R.id.hostFragment)
        NavigationUI.setupWithNavController(binding.btmNavCon,navController)

    }

}