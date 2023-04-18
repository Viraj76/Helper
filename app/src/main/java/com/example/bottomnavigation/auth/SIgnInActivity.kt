package com.example.bottomnavigation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation.client.activity.ClientMainActivity
import com.example.bottomnavigation.contractor.activity.ContractorMainActivity
import com.example.bottomnavigation.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*



class SIgnInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var existedContractorId : String
    private lateinit var existedClientId : String
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = binding.progressBar
        binding.btnLogin.setOnClickListener { loggingUser() }
        binding.tvSignUp.setOnClickListener { goingToSignUpActivity() }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
            finish()
        }

    }

    private fun goingToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loggingUser() {
        firebaseAuth = FirebaseAuth.getInstance()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()


        if (email.isNotEmpty() && password.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val verifyEmail = firebaseAuth.currentUser?.isEmailVerified
                        if(verifyEmail == true){
                            // just current user id who wants to log in
                            val currentUserId = firebaseAuth.currentUser?.uid
                            //initializing existedClientId and existedContractorId
                            existedClientId = "random"
                            existedContractorId = "random"
                            val clientDatabaseReference = FirebaseDatabase.getInstance().getReference("Clients Id's")
                            clientDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for(childrenSnapshot in snapshot.children){
                                        val clientsIds = childrenSnapshot.child("clientId").value.toString()
                                        if(clientsIds == currentUserId){
                                            existedClientId=clientsIds
                                            break
                                        }
                                    }
                                    if(existedClientId == currentUserId) {
                                        val intent = Intent(this@SIgnInActivity, ClientMainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        progressBar.visibility = View.GONE
                                        Toast.makeText(this@SIgnInActivity, "Signed In Successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                            val contractorsRef = FirebaseDatabase.getInstance().getReference("Contractor Id's")
                            contractorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (childSnapshot in dataSnapshot.children) {
                                        val contractorIds = childSnapshot.child("contractorId").value.toString()
                                        if(contractorIds == currentUserId){
                                            existedContractorId=contractorIds
                                            break
                                        }
                                    }
                                    if( existedContractorId== currentUserId){
                                        val intent = Intent(this@SIgnInActivity, ContractorMainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        progressBar.visibility = View.GONE
                                        Toast.makeText(this@SIgnInActivity, "Signed In Successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                        else{
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Email is not verified", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }

        else {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }
    } }




