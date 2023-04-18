package com.example.bottomnavigation.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation.databinding.ActivitySignUpBinding
import com.example.bottomnavigation.models.ClientID
import com.example.bottomnavigation.models.ContractorID

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var radioGroup: RadioGroup
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private  var userPreferences: String?=null
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        progressBar.visibility = View.GONE
        firebaseAuth = FirebaseAuth.getInstance()
        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, checkedId -> storingUserTypeInSharedReferences(checkedId) }
            btnRegister.setOnClickListener { createNewUser() }
            tvSignIn.setOnClickListener { goingToSignInActivity() }
        }



    }

    private fun storingUserTypeInSharedReferences(checkedId:Int) {
        val radioButton = findViewById<RadioButton>(checkedId)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("user_preference", radioButton.text.toString())
        editor.apply()
    }
    private fun goingToSignInActivity() {
        val intent = Intent(this, SIgnInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createNewUser() {
//        progressBar.visibility = View.VISIBLE
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userPreferences = sharedPreferences.getString("user_preference","")

        if(userPreferences.equals("Client")){
            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
               if(password == confirmPassword){
                   databaseReference = FirebaseDatabase.getInstance().getReference("Clients Id's")
                   firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                       if(task.isSuccessful){
                           firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                               Toast.makeText(this,"Please check your mail to VERIFY (spam also)",Toast.LENGTH_SHORT).show()
                               val uId = task.result.user?.uid.toString()
                               val userClient = ClientID(name,email,password,uId)
                               databaseReference.child(uId).setValue(userClient)
                               val intent = Intent(this, SIgnInActivity::class.java)
                               startActivity(intent)
                               finish()
                           }
                               ?.addOnFailureListener {
                                   Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                               }
//                           val intent = Intent(this, SIgnInActivity::class.java)
//                           startActivity(intent)
                       }
                       else{
                           Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
                       }
                   }
               }
                else{
                   Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
               }
            }
            else{
                Toast.makeText(this,"Empty fields are not allowed",Toast.LENGTH_SHORT).show()
            }
        }


        if(userPreferences.equals("Contractor")){
            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(password == confirmPassword){
                    databaseReference = FirebaseDatabase.getInstance().getReference("Contractor Id's")

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener{
                                Toast.makeText(this,"Please check your mail to VERIFY (spam also)",Toast.LENGTH_LONG).show()
                                val uId = task.result.user?.uid.toString()
                                val userContractor = ContractorID(name,email,password,uId)
                                databaseReference.child(uId).setValue(userContractor)
                                val intent = Intent(this, SIgnInActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                                ?.addOnFailureListener {
                                    Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                                }
                        }
                        else{
                            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Empty fields are not allowed",Toast.LENGTH_SHORT).show()
            }
        }




    }


}
