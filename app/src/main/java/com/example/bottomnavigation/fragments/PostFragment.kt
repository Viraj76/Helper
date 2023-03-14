
package com.example.bottomnavigation.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bottomnavigation.ClientDetails
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.FragmentPostBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater)

        binding.btnPost.setOnClickListener {

                val firstName = binding.tvIFirstName.text.toString()
                val middleName = binding.tvMiddleName.text.toString()
                val lastName = binding.tvLastName.text.toString()
                val pinCode = binding.tvPinCode.text.toString()
                val state = binding.tvState.text.toString()
                val city = binding.tvCity.text.toString()
                val briefDetail = binding.tvBriefDetail.text.toString()

                val name = "$firstName $middleName $lastName"
                val address = "$pinCode, $state, $city, $briefDetail"
                val description = binding.tvDescription.text.toString()

                databaseReference = FirebaseDatabase.getInstance().getReference("Client Detail")

                val clientDetail = ClientDetails(name,address,description)

                databaseReference
                    .child(name)
                    .setValue(clientDetail)
                    .addOnSuccessListener {
                        binding.tvIFirstName.text?.clear()
                        binding.tvMiddleName.text?.clear()
                        binding.tvLastName.text?.clear()
                        binding.tvPinCode.text?.clear()
                        binding.tvState.text?.clear()
                        binding.tvCity.text?.clear()
                        binding.tvBriefDetail.text?.clear()
                        binding.tvDescription.text?.clear()
                        Toast.makeText(requireContext(),"Your Need is Posted!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        return binding.root
    }
}

