package com.example.bottomnavigation.client.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.chat.ChatActivity
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
        quotationsAdapter = NotificationAdapter(requireContext(),::onRejectButtonClick, ::onAcceptButtonClick)
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
                        if(quotationsSnapshot.key!!.contains(currentUser!!)){
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
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun onAcceptButtonClick(quotations : Quotations){
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        builder.apply {
            setTitle("Accept Quotation")
            setMessage("Are you sure you want to accept this quotation?")
            setPositiveButton("Yes") { dialogInterface, which ->
                deleteQuotation(quotations)
                setARoomForChat(quotations)
//
            }
            setNegativeButton("No") { dialogInterface, which ->
                alertDialog.dismiss()
            }
            show()
            setCancelable(false)
        }
    }

    private fun setARoomForChat(quotations: Quotations) {
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("id",quotations.contractorUserId)
        startActivity(intent)
//        requireActivity().finish()
    }

    private fun onRejectButtonClick(quotations : Quotations){
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        builder.apply {
            setTitle("Reject Quotation")
            setMessage("Are you sure you want to reject this quotation?")
            setPositiveButton("Yes") { dialogInterface, which ->
                deleteQuotation(quotations)
            }
            setNegativeButton("No") { dialogInterface, which ->
                alertDialog.dismiss()
            }
            show()
            setCancelable(false)
        }
    }

    private fun deleteQuotation(quotations: Quotations) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val contractorId = quotations.contractorUserId.toString()
        val deletingRoom = contractorId + currentUserId
        FirebaseDatabase.getInstance().getReference("Quotations")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(allRooms in snapshot.children){
                        if(deletingRoom == allRooms.key){
                            allRooms.ref.removeValue()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}