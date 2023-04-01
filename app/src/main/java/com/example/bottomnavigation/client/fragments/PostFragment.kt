package com.example.bottomnavigation.client.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.bottomnavigation.R
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnPost.setOnClickListener {

            val firstName = binding.tvIFirstName.text.toString()
            val middleName = binding.tvMiddleName.text.toString()
            val lastName = binding.tvLastName.text.toString()
            val pinCode = binding.tvPinCode.text.toString()
            val state = binding.tvState.text.toString()
            val city = binding.tvCity.text.toString()
            val briefDetail = binding.tvBriefDetail.text.toString()

            val name = "${firstName.trim()} ${middleName.trim()} ${lastName.trim()}"
            val address = "$pinCode, $state, $city, $briefDetail"
            val description = binding.tvDescription.text.toString()
            val currentUserId = firebaseAuth.currentUser?.uid

            databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
            val clientDetail = ClientPosts(currentUserId,name, address, description)
            if (firstName.isNotEmpty() && lastName.isNotEmpty()
                && pinCode.isNotEmpty() && state.isNotEmpty()
                && city.isNotEmpty() && description.isNotEmpty()
                && description.isNotEmpty()
            ) {
                if (currentUserId != null) {
                    databaseReference
                        .push()
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
                            Toast.makeText(requireContext(), "Your Need is Posted!", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_postFragment_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                                .show()

                        }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Fill All Required Fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }
}

