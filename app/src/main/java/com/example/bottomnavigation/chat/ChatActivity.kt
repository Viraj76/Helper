package com.example.bottomnavigation.chat

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.contractor.activity.ContractorMainActivity
import com.example.bottomnavigation.databinding.ActivityChatBinding
import com.example.bottomnavigation.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChatBinding
    private lateinit var clientName : String
    private lateinit var clientId : String
    private lateinit var messageList : ArrayList<Message>
    private lateinit var chatActivityAdapter: ChatActivityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        showingShareProfileOption()
        setContentView(binding.root)
        initializations()
        prepareRvForChatActivity()
        showingMessages()
        binding.ivSendChat.setOnClickListener {
            val message = binding.tvMessage.text.toString()
            binding.tvMessage.text.clear()
            if(message.isEmpty()) Toast.makeText(this,"Enter a message please",Toast.LENGTH_SHORT).show()
            else storingMessage(message)
        }

        binding.tvMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val lines = binding.tvMessage.lineCount
                val desiredLines = 2
                val layoutParams = binding.tvMessage.layoutParams

                if (lines <= desiredLines) {
                    layoutParams.height = 45.dpToPx()
                } else {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }

                binding.tvMessage.layoutParams = layoutParams
            }
        })

    }

    private fun showingShareProfileOption() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        var existedContractorId = "random"
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
                    binding.ivShareProfile.visibility = View.VISIBLE
                }
                else{
                    binding.ivShareProfile.visibility = View.GONE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ChatActivity, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private var contractorId : String? = null
    private var chatRoomId : String? = null
    fun Int.dpToPx(): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
    private fun storingMessage(message: String) {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        val map = hashMapOf<String,String>()
        map["message"] = message
        map["senderId"] = contractorId!!
        map["currentDate"] = currentDate
        map["currentTime"] = currentTime

        FirebaseDatabase.getInstance().getReference("Chatbase").child(chatRoomId!!).push().setValue(map)
            .addOnCompleteListener {
                if(it.isSuccessful)
                     Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show()
            }
    }

    private fun showingMessages() {
        val clientId = intent.getStringExtra("id")
        contractorId = FirebaseAuth.getInstance().currentUser?.uid
        chatRoomId = contractorId +clientId
        val reverseOfChatRoomId  = clientId+   contractorId     //needed for adding the respective chat of client in the same roomChatId that a contractor has created
        val chatbaseReference = FirebaseDatabase.getInstance().getReference("Chatbase")
        chatbaseReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(chatRoomId!!)){
                    getMessages(chatRoomId!!)
                }
                else if(snapshot.hasChild(reverseOfChatRoomId)){
                    chatRoomId = reverseOfChatRoomId
                    getMessages(chatRoomId!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getMessages(chatRoomId: String) {
        FirebaseDatabase.getInstance().getReference("Chatbase").child(chatRoomId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messagesList = ArrayList<Message>()
                    for(messages in snapshot.children){
                        val message  = messages.getValue(Message::class.java)
                        messagesList.add(message!!)
                    }
                    chatActivityAdapter.setMessageList(messagesList)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun prepareRvForChatActivity() {
        chatActivityAdapter = ChatActivityAdapter(binding.rvChats, this)
        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = chatActivityAdapter
        }
    }

    private fun initializations() {
        messageList  = ArrayList()
        clientName = intent.getStringExtra("name").toString()
        clientId = intent.getStringExtra("id").toString()
    }











}