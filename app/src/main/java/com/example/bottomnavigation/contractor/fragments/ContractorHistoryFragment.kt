package com.example.bottomnavigation.contractor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.contractor.adapter.RatingHistoryAdapter
import com.example.bottomnavigation.databinding.FragmentContractorHistoryBinding
import com.example.bottomnavigation.models.ContractorID
import com.example.bottomnavigation.models.RatedContractor
import com.example.bottomnavigation.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class ContractorHistoryFragment : Fragment() {

    private lateinit var binding : FragmentContractorHistoryBinding
    private lateinit var ratingHistoryAdapter : RatingHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContractorHistoryBinding.inflate(layoutInflater)
        Config.showDialog(requireContext())
        showingContractorName()
        prepareRatingHistoryAdapterRv()
        showingClientWhoRated()
        return binding.root
    }

    private fun showingClientWhoRated() {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("Rated Contractor").child(currentUser!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clientList = ArrayList<RatedContractor>()
                    for(clients in snapshot.children){
                        val clientData = clients.getValue(RatedContractor::class.java)
                        clientList.add(clientData!!)
                    }
                    Config.hideDialog()
                    ratingHistoryAdapter.setClientWhoRated(clientList)
                }
                override fun onCancelled(error: DatabaseError) {
                    Config.hideDialog()
                    TODO("Not yet implemented")
                }
            })
//        clientListWhoRated(currentUser!!, object : ClientWhoRatedListCallBack{
//            override fun onGettingClientListWhoRated(clientList: ArrayList<RatedContractor>) {
//                ratingHistoryAdapter.setClientWhoRated(clientList)
//            }
//
//        })
    }
    private fun prepareRatingHistoryAdapterRv() {
        ratingHistoryAdapter = RatingHistoryAdapter()
        binding.ratingRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = ratingHistoryAdapter
        }
    }

    private fun showingContractorName() {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("Contractor Id's").child(currentUser!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contractorDetail = snapshot.getValue(ContractorID::class.java)
                    binding.contractorName.text = contractorDetail?.name
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun clientListWhoRated(currentUser : String, callBack : ClientWhoRatedListCallBack){
        FirebaseDatabase.getInstance().getReference("Rated Contractor").child(currentUser)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clientList = ArrayList<RatedContractor>()
                    for(clients in snapshot.children){
                        val clientData = clients.getValue(RatedContractor::class.java)
                        clientList.add(clientData!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    interface ClientWhoRatedListCallBack{
        fun onGettingClientListWhoRated(clientList : ArrayList<RatedContractor>)
    }
}