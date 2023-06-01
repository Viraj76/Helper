package com.example.bottomnavigation.contractor.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.bottomnavigation.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.auth.SIgnInActivity
import com.example.bottomnavigation.contractor.adapter.ContractorPostAdapter
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.FragmentHome2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContractorHomeFragment : Fragment() {

    private lateinit var binding:FragmentHome2Binding
    private lateinit var contractorPostAdapter: ContractorPostAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var  retrievedPosts : ArrayList<ClientPosts>
    private lateinit var context:Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHome2Binding.inflate(inflater)

        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrievedPosts =  ArrayList()
        prepareContractorPostRecyclerView()
        showingPostsToContractor()
        binding.contractorHomeToolbar.inflateMenu(R.menu.client_main_activity)
        binding.contractorHomeToolbar.setOnMenuItemClickListener(){
            when(it.itemId){
                R.id.logOut ->{
                    val builder = AlertDialog.Builder(requireContext())
                    val alertDialog = builder.create()
                    builder
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes"){dialogInterface,which->
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(requireContext(), SIgnInActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        .setNegativeButton("No"){dialogInterface, which->
                            alertDialog.dismiss()
                        }
                        .show()
                        .setCancelable(false)
                    true
                }
                else -> {false}
            }
        }
    }


    private fun showingPostsToContractor() {
        databaseReference = FirebaseDatabase.getInstance().getReference("All Posts")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (allClientsData in snapshot.children) {
                        val clientsData = allClientsData.getValue(ClientPosts::class.java)
                        retrievedPosts.add(clientsData!!)
                    }
                    contractorPostAdapter.setPosts(retrievedPosts)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun prepareContractorPostRecyclerView() {
        contractorPostAdapter = ContractorPostAdapter(requireContext())
        binding.rvContractorPost.apply {
            layoutManager  = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = contractorPostAdapter
        }
    }
//    private fun onChatOptionClicked(name : String, userId : String){
//        val intent = Intent(requireContext(),ChatActivity::class.java)
//        intent.putExtra("user",name)
//        intent.putExtra("id",userId)
//        startActivity(intent)
////        val bundle = Bundle()
////        bundle.putString("clientName",name)
////        findNavController().navigate(R.id.action_homeFragment3_to_contractorChatFragment,bundle)
//    }



}