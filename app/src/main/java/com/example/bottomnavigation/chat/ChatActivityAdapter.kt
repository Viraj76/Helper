package com.example.bottomnavigation.chat

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.contractor.activity.ContractorProfileActivity
import com.example.bottomnavigation.databinding.ReceiveMessageBinding
import com.example.bottomnavigation.databinding.SendMessageBinding
import com.example.bottomnavigation.databinding.ShareProfileReceiveBinding
import com.example.bottomnavigation.databinding.ShareProfileSendBinding

import com.example.bottomnavigation.models.Message
import com.google.firebase.auth.FirebaseAuth


class ChatActivityAdapter(val recyclerView: RecyclerView, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private  var messageList = ArrayList<Message>()
    private var SENT_MESSAGE = 1
    private var RECEIVED_MESSAGE = 2
    private var SHARE_PROFILE_SEND = 3
    private var SHARE_PROFILE_RECEIVE = 4

    fun setMessageList(messageList : ArrayList<Message>) {
        this.messageList = messageList
        notifyDataSetChanged()
        recyclerView.post {
            recyclerView.smoothScrollToPosition(itemCount - 1)
        }
    }

    class SendViewHolder(val binding: SendMessageBinding): ViewHolder(binding.root)
    class ReceiveViewHolder(val binding: ReceiveMessageBinding): ViewHolder(binding.root)
    class ShareProfileSend (val binding: ShareProfileSendBinding): ViewHolder(binding.root)
    class ShareProfileReceive(val binding: ShareProfileReceiveBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SENT_MESSAGE -> {
                SendViewHolder(SendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            RECEIVED_MESSAGE -> {
                ReceiveViewHolder(ReceiveMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            SHARE_PROFILE_SEND -> {
                ShareProfileSend(ShareProfileSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            SHARE_PROFILE_RECEIVE -> {
                ShareProfileReceive(ShareProfileReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        if(holder.javaClass == SendViewHolder::class.java){
            holder as SendViewHolder
            holder.binding.tvSendMessage.text = message.message
        }
        else if(holder.javaClass == ReceiveViewHolder::class.java){
            holder as ReceiveViewHolder
            holder.binding.tvReceiveMessage.text = message.message
        }
        else if (holder.javaClass == ShareProfileSend::class.java){
            holder as ShareProfileSend
            holder.binding.btnRateMe.text = message.message

        }
        else{
            holder as ShareProfileReceive
            holder.binding.btnRateMe.text = message.message
            holder.binding.btnRateMe.setOnClickListener {
                val intent = Intent(context,ContractorProfileActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val currentMessage = messageList[position]
        val isProfileSharing = currentMessage.profileSharing
        return if (isProfileSharing.equals("true")) {
            if (isProfileSharing != null) {
                Log.d("ss",isProfileSharing)
            }
            if (currentUser == currentMessage.senderId) SHARE_PROFILE_SEND
            else SHARE_PROFILE_RECEIVE
        } else {
            if (currentUser == currentMessage.senderId) SENT_MESSAGE
            else RECEIVED_MESSAGE
        }
    }
    }


