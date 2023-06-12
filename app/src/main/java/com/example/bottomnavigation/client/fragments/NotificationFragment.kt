package com.example.bottomnavigation.client.fragments

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.client.adapter.NotificationAdapter
import com.example.bottomnavigation.databinding.FragmentNotificationBinding
import com.example.bottomnavigation.models.Quotations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding
    private lateinit var quotationsAdapter: NotificationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        prepareRvForQuotationAdapter()
        showingTheQuotations()
        return binding.root
    }

    private fun prepareRvForQuotationAdapter() {
        quotationsAdapter = NotificationAdapter(this)
        binding.rvQuotations.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = quotationsAdapter
        }
    }

    private fun showingTheQuotations() {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("aa",currentUser.toString())
        FirebaseDatabase.getInstance().getReference("Quotations")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val quotationsList = ArrayList<Quotations>()
                    for (quotationsSnapshot in snapshot.children) {
                        val room = quotationsSnapshot.key
                        FirebaseDatabase.getInstance().getReference("Quotations").child(room!!)
                            .addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    for(data in snapshot.children){
                                        val hh = data.getValue(Quotations::class.java)
                                        quotationsList.add(hh!!)
                                        Log.d("bb",hh.toString())
                                    }
                                    Log.d("vv",quotationsList.toString())
                                    quotationsAdapter.setQuotationsLists(quotationsList)
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}