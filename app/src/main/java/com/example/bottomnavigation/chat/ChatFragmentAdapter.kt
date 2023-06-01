package com.example.bottomnavigation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.databinding.ChatItemViewBinding
import com.example.bottomnavigation.models.ClientID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragmentAdapter(val context: Context) : RecyclerView.Adapter<ChatFragmentAdapter.UserListViewHolder>() {

    private var clientIdList = ArrayList<String>()
    private var chatRoom = ArrayList<String>()

    fun setClientIdList(clientIdList : ArrayList<String>){
        this.clientIdList = clientIdList
        notifyDataSetChanged()
    }
    fun setChatRoom(chatRoom : ArrayList<String>){
        this.chatRoom = chatRoom
        notifyDataSetChanged()
    }


    class UserListViewHolder(val binding: ChatItemViewBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(ChatItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val clientId = clientIdList[position]
        FirebaseDatabase.getInstance().getReference("Clients Id's").child(clientId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val clientDetails = snapshot.getValue(ClientID::class.java)
                        holder.binding.userName.text = clientDetails?.name
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        holder.itemView.setOnClickListener {
            val intent = Intent(context , ChatActivity::class.java)
            intent.putExtra("id",clientIdList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return clientIdList.size
    }

}