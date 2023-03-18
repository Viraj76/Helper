package com.example.bottomnavigation.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation.ClientID
import com.example.bottomnavigation.ContractorID
import com.example.bottomnavigation.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SIgnInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userPreferences: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var contractorId : String
    private lateinit var clientId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userPreferences = sharedPreferences.getString("user_preference", "")
        binding.radioGroup.setOnCheckedChangeListener{_,checkedId->  storingUserTypeInSharedReferences(checkedId) }
        binding.btnLogin.setOnClickListener { loggingUser() }
        binding.tvSignUp.setOnClickListener { goingToSignUpActivity() }


    }

    private fun storingUserTypeInSharedReferences(checkedId: Int) {
        val radioButton = findViewById<RadioButton>(checkedId)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("user_preference", radioButton.text.toString())
        editor.apply()
    }

    private fun goingToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }


    private fun loggingUser() {
        firebaseAuth = FirebaseAuth.getInstance()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUserId = firebaseAuth.currentUser?.uid
                        //                            var clientId =""


                        if (currentUserId != null) {
                            clientId = currentUserId
                        }
                        val clientDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Clients Id's")
                            clientDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for(childrenSnapshot in snapshot.children){
                                        val contractorIds = childrenSnapshot.child("contractorId").value.toString()
                                    if(contractorIds == currentUserId){
                                        contractorId=contractorIds
                                    }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

//                        val conList= ArrayList<String>()
//                        val contractorsRef = FirebaseDatabase.getInstance().getReference("Contractor Id's")
//                        contractorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                for (childSnapshot in dataSnapshot.children) {
//                                    val contractorIds = childSnapshot.child("contractorId").getValue().toString()
//                                    if(contractorIds == currentUserId){
//                                        contractorId=contractorIds
//                                    }
//
//                                }
//
//                            }
//
//                            override fun onCancelled(databaseError: DatabaseError) {
//                                TODO("Not yet implemented")
//                            }
//                        })
//
//                        Log.d("lll",contractorId)

//                            if(currentUserId == clientId) {
//                                val intent = Intent(this, MainActivity::class.java)
//                                startActivity(intent)
//                            }else{
//                                binding.etEmail.setText(currentUserId)
//                                binding.etPassword.setText(clientId)
//                            }
                        if(currentUserId == clientId){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            binding.etEmail.setText(currentUserId)
                            binding.etPassword.setText(contractorId)
                        }
//                            println(clientId)
//                            println(contractorId)
                        Toast.makeText(this, "Signed In Successfully!", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
        } else {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }

    } }




