package com.example.bottomnavigation

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.contractor.activity.ContractorMainActivity
import com.example.bottomnavigation.contractor.adapter.ContractorChatAdapter
import com.example.bottomnavigation.databinding.ActivityChatBinding
import com.example.bottomnavigation.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ChatActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChatBinding
    private lateinit var chatToolbar: Toolbar
    private lateinit var clientName : String
    private lateinit var clientId : String
    private  var senderRoom:String?=null
    private  var receiverRoom:String?=null
    private lateinit var messageList : ArrayList<Message>
    private lateinit var contractorChatAdapter: ContractorChatAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var contractorId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList  = ArrayList()
        clientName = intent.getStringExtra("name").toString()
        clientId = intent.getStringExtra("id").toString()
        contractorId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        senderRoom = "$clientId $contractorId"
        receiverRoom = "$contractorId $clientId"


        settingUpToolBar()
        prepareRvForChar()

        chattingImplementation()

    }



    private fun chattingImplementation() {

    }

    private fun prepareRvForChar() {
        contractorChatAdapter = ContractorChatAdapter(binding.rvChats)
        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = contractorChatAdapter
        }
    }
    private fun settingUpToolBar() {

        binding.chatToolBar.apply {
            title = clientName
            setTitleTextColor(resources.getColor(R.color.white))
            setSupportActionBar(this)
        }


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back) // replace with your own back button icon
            // Set the color filter to change the color of the icon
            val color = ContextCompat.getColor(this@ChatActivity, R.color.white)
            val backArrow = ContextCompat.getDrawable(this@ChatActivity, R.drawable.back)
            backArrow?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            setHomeAsUpIndicator(backArrow)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                val intent  = Intent(this,ContractorMainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
}