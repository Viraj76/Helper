package com.example.bottomnavigation.contractor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.FragmentContractorChatBinding

class ContractorChatFragment : Fragment() {

    private lateinit var binding: FragmentContractorChatBinding
    private lateinit var historyToolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContractorChatBinding.inflate(layoutInflater)

        return binding.root
    }



}