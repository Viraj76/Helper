package com.example.bottomnavigation.client.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.client.adapter.HistoryAdapter
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentClientDatabaseReference: DatabaseReference
    private lateinit var clientPostHistoryAdapter: HistoryAdapter
    private lateinit var clientPostHistoryList : ArrayList<ClientPosts>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHistoryBinding.inflate(layoutInflater)
        binding.historyToolbar.apply {
            title = "History"
            (activity as AppCompatActivity).setSupportActionBar(this)
        }
        initialization()
        prepareRecyclerViewForHistory()
        gettingCurrentClientPostHistory()
        return binding.root
    }
    private fun prepareRecyclerViewForHistory() {
        binding.rvClientPostHistory.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = clientPostHistoryAdapter
        }
    }

    private fun gettingCurrentClientPostHistory() {
        val currentClientId = firebaseAuth.currentUser?.uid
        currentClientDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(clientPost in snapshot.children){
                    val clientId  = clientPost.child("userId").value.toString()
                    if(clientId == currentClientId){
                        clientPost.getValue(ClientPosts::class.java)
                            ?.let { clientPostHistoryList.add(it) }
                    }
                }
                clientPostHistoryAdapter.setClientHistory(clientPostHistoryList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initialization() {
        firebaseAuth = FirebaseAuth.getInstance()
        currentClientDatabaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
        clientPostHistoryAdapter = HistoryAdapter()
        clientPostHistoryList = ArrayList()
    }
}