package com.example.bottomnavigation.contractor.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bottomnavigation.databinding.ActivityQuestionsBinding
import com.example.bottomnavigation.models.Quotations
import com.example.bottomnavigation.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding
    private val contractorId = FirebaseAuth.getInstance().currentUser?.uid
    private var quotationImageURI:Uri? = null
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        quotationImageURI = it
        binding.ivQuotationImageView.setImageURI(quotationImageURI)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contractorHomeToolbar.apply {
            setSupportActionBar(this)
            setNavigationOnClickListener {
                startActivity(Intent(this@QuestionsActivity, ContractorMainActivity::class.java))
                finish()
            }
        }
        binding.ivSelectFile.setOnClickListener {
            selectImage.launch("image/*")
        }
        binding.btnSubmit.setOnClickListener {
            uploadQuotationImage()
        }
    }

    private fun uploadQuotationImage() {
        Config.showDialog(this)
        if (quotationImageURI == null) {
            // No image selected
            creatingChatBase("")
        } else {
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid
            val storageReference = FirebaseStorage.getInstance().getReference("Quotation Image")
                .child(currentUser!!)

            storageReference.putFile(quotationImageURI!!).addOnSuccessListener {
                storageReference.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        creatingChatBase(downloadUri.toString())
                    }
                    .addOnFailureListener {
                        Config.hideDialog()
                    }
            }
                .addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun creatingChatBase(quotationImageURI: String) {
        val clientId = intent.getStringExtra("id")
        val chatRoomId = contractorId + clientId
        val ques1 = binding.ques1.text.toString()
        val ques2 = binding.ques2.text.toString()
        val ques3 = binding.ques3.text.toString()
        val ques4 = binding.ques4.text.toString()
        val ans1 = binding.tvAnswer1.text.toString()
        val ans2 = binding.tvAnswer2.text.toString()
        val ans3 = binding.tvAnswer3.text.toString()
        val ans4 = binding.tvAnswer4.text.toString()
        val greetMessage = "Hey there! hope you are doing well\n $ques1 - $ans1,\n $ques2 - $ans2,\n $ques3 - $ans3,\n $ques4 - $ans4"
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if(ans1.isNotEmpty() && ans2.isNotEmpty() && ans3.isNotEmpty() && ans4.isNotEmpty()){
            FirebaseDatabase.getInstance().getReference("Contractor Id's").child(currentUserId!!).child("name")
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val contractName = snapshot.getValue(String::class.java)
                        FirebaseDatabase.getInstance().getReference("Rated Contractor").child(currentUserId)
                            .addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var contractorRating: Float = 0f
                                    var ratingCount: Int = 0
                                    for (ratingSnapshot in snapshot.children) {
                                        val ratingValue = ratingSnapshot.child("rating").getValue(String::class.java)
                                        if (ratingValue != null) {
                                            contractorRating += ratingValue.toFloat()
                                            ratingCount++
                                        }
                                    }
                                    if (ratingCount > 0) {
                                        contractorRating /= ratingCount
                                        contractorRating = contractorRating.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toFloat()
                                    }
                                    val notificationDetail = Quotations(currentUserId,contractName,greetMessage,currentDate,contractorRating.toString(), quotationImage = quotationImageURI.toString())

                                    FirebaseDatabase.getInstance().getReference("Quotations").child(chatRoomId).push()
                                        .setValue(notificationDetail)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Config.hideDialog()
                                                val intent = Intent(this@QuestionsActivity, ContractorMainActivity::class.java)
                                                intent.putExtra("id", clientId)
                                                startActivity(intent)
                                                finish()
                                                Toast.makeText(this@QuestionsActivity, "Client will contact you soon!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Config.hideDialog()
                                    TODO("Not yet implemented")
                                }

                            })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Config.hideDialog()
                        TODO("Not yet implemented")
                    }

                })
        }
        else{
            Config.hideDialog()
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show()
        }



    }
}