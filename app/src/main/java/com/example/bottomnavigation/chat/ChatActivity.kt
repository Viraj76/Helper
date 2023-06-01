package com.example.bottomnavigation

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
    private lateinit var chatToolbar: Toolbar
    private lateinit var clientName : String
    private lateinit var clientId : String
    private lateinit var messageList : ArrayList<Message>
    private lateinit var chatActivityAdapter: ChatActivityAdapter
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
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
    }

    private var contractorId : String? = null
    private var chatRoomId : String? = null

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