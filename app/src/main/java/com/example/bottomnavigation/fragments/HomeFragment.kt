package com.example.bottomnavigation.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.ClientDetails
import com.example.bottomnavigation.PostAdapter
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.FragmentHomeBinding
import com.example.bottomnavigation.databinding.PostCardBinding
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var postAdapter: PostAdapter
    private lateinit var allClientsDataList : ArrayList<ClientDetails>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        allClientsDataList = ArrayList()
        preparePostRecyclerView()
        showingPostInTheRecyclerView()
        return binding.root

    }

    private fun showingPostInTheRecyclerView() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Client Detail")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(allClientsData in snapshot.children){
                        val clientsData = allClientsData.getValue(ClientDetails::class.java)
                        allClientsDataList.add(clientsData!!)
                    }
                    postAdapter.setPostList(allClientsDataList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun preparePostRecyclerView() {
        postAdapter = PostAdapter()
        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = postAdapter
        }
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val toolbar = view.findViewById<Toolbar>(R.id.toolBar)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//    }
}