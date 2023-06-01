package com.example.bottomnavigation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.contractor.adapter.ContractorChatAdapter
import com.example.bottomnavigation.databinding.ReceiveMessageBinding
import com.example.bottomnavigation.databinding.SendMessageBinding
import com.example.bottomnavigation.models.Message
import com.google.firebase.auth.FirebaseAuth


class ChatActivityAdapter(val recyclerView: RecyclerView, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

   private  var messageList = ArrayList<Message>()

    var SENT_MESSAGE = 1
    var RECEIVED_MESSAGE = 2

    fun setMessageList(messageList : ArrayList<Message>) {
        this.messageList = messageList
        notifyDataSetChanged()
        recyclerView.post {
            recyclerView.smoothScrollToPosition(itemCount - 1)
        }
    }


    class SendViewHolder(val binding: SendMessageBinding): ViewHolder(binding.root)
    class ReceiveViewHolder(val binding: ReceiveMessageBinding): ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == SENT_MESSAGE)
            SendViewHolder(SendMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else
                ReceiveViewHolder(ReceiveMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if(holder.javaClass == SendViewHolder::class.java){
            holder as SendViewHolder
            holder.binding.tvSendMessage.text = message.message
        }
        else{
            holder as ReceiveViewHolder
            holder.binding.tvReceiveMessage.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val currentMessage = messageList[position]
        return if(currentUser == currentMessage.senderId) SENT_MESSAGE
        else
            RECEIVED_MESSAGE
    }

}