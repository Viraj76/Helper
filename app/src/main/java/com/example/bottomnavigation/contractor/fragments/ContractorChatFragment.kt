package com.example.bottomnavigation.contractor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.chat.ChatFragmentAdapter
import com.example.bottomnavigation.contractor.viewModels.ContractorChatViewModel
import com.example.bottomnavigation.databinding.FragmentContractorChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContractorChatFragment : Fragment() {

    private lateinit var binding: FragmentContractorChatBinding
    private lateinit var historyToolbar: Toolbar
    private lateinit var clientList: ArrayList<String>
    private lateinit var chatFragmentAdapter: ChatFragmentAdapter
//    private val viewModel by viewModels<ContractorChatViewModel>()
    private lateinit var viewModel: ContractorChatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ContractorChatViewModel::class.java]
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContractorChatBinding.inflate(layoutInflater)



        prepareRvForContractorChatAdapter()
        addingClient()
        gettingClientWithWhomTheContractorHasChatted()



        return binding.root
    }



    private fun prepareRvForContractorChatAdapter() {
        chatFragmentAdapter = ChatFragmentAdapter(requireContext())
        binding.rvClientsForChat.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,  false)
            adapter = chatFragmentAdapter
        }
    }

    private fun gettingClientWithWhomTheContractorHasChatted() {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("Chatbase")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clientsIdList = ArrayList<String>()
                    val chatRoomList = ArrayList<String>()
                    for(chatIds in snapshot.children){
                        if(chatIds.key!!.contains(currentUserId!!)){
                            chatRoomList.add(chatIds.key!!)
                            clientsIdList.add(chatIds.key!!.replace(currentUserId,""))
                        }
                    }
                    chatFragmentAdapter.setClientIdList(clientsIdList)
                    chatFragmentAdapter.setChatRoom(chatRoomList)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }



    private fun addingClient() {
        val clientName = arguments?.getString("clientName")
        if (clientName != null) {
            viewModel.addClientToTheList(clientName)
        }
    }





}