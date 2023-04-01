package com.example.bottomnavigation.contractor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.models.Message
import com.example.bottomnavigation.databinding.ReceiveMessageBinding
import com.example.bottomnavigation.databinding.SendMessageBinding
import com.google.firebase.auth.FirebaseAuth

class ContractorChatAdapter(val recyclerView: RecyclerView):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList = ArrayList<Message>()
    private val ITEM_RECEIVE = 1
    private val ITEM_SENT =2

    fun setMessageList(messageList : ArrayList<Message>){
        this.messageList = messageList
        notifyDataSetChanged()
        recyclerView.post {
            // Scroll to the last item in the RecyclerView
            recyclerView.smoothScrollToPosition(itemCount - 1)
        }
    }

    class SentViewHolder(val binding:SendMessageBinding):ViewHolder(binding.root)
    class ReceiveViewHolder(val binding:ReceiveMessageBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            ReceiveViewHolder(ReceiveMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else{
            SentViewHolder(SendMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage =  messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.binding.tvSendMessage.text = currentMessage.message
        }
        else{
            val viewHolder  = holder as ReceiveViewHolder
            holder.binding.tvReceiveMessage.text = currentMessage.message
        }


    }

    override fun getItemCount(): Int {
     return  messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val currentMessage = messageList[position]
        return if(currentUid == currentMessage.senderId) ITEM_SENT
        else ITEM_RECEIVE
    }
}