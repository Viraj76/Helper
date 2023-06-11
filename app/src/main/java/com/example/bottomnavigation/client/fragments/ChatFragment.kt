package com.example.bottomnavigation.client.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.R
import com.example.bottomnavigation.chat.NotificationFragmentAdapter
import com.example.bottomnavigation.databinding.FragmentChatBinding
import com.example.bottomnavigation.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private lateinit var notificationFragmentAdapter: NotificationFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        prepareChatFragmentAdapterForShowingContractors()
        showingContractors()
        return binding.root
    }
    private fun prepareChatFragmentAdapterForShowingContractors() {
        notificationFragmentAdapter = NotificationFragmentAdapter(requireContext())
        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = notificationFragmentAdapter
        }
    }
    private fun showingContractors() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("Chatbase")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clientsIdList = ArrayList<String>()
                    val chatRoomList = ArrayList<String>()
                    for(chatIds in snapshot.children){
                        if(chatIds.key!!.contains(currentUserId!!)){
                            chatRoomList.add(chatIds.key!!)
                            clientsIdList.add(chatIds.key!!.replace(currentUserId,""))
                        }
                    }
                    notificationFragmentAdapter.setClientIdList(clientsIdList)
                    notificationFragmentAdapter.setChatRoom(chatRoomList)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}