package com.example.bottomnavigation.contractor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.ClientViewAdapter
import com.example.bottomnavigation.contractor.viewModels.ContractorChatViewModel
import com.example.bottomnavigation.databinding.FragmentContractorChatBinding

class ContractorChatFragment : Fragment() {

    private lateinit var binding: FragmentContractorChatBinding
    private lateinit var historyToolbar: Toolbar
    private lateinit var clientList: ArrayList<String>
    private lateinit var clientViewAdapter: ClientViewAdapter
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

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareRvForClientViewAdapter()
        addingClient()
        observeClientsListLiveData()

//        clientName = arguments?.getString("clientName").toString()
//        clientList.add(clientName)
//        clientViewAdapter.setClientInfo(clientList)
//        clientName?.let {
//            clientList.add(it)
//            clientViewAdapter.setClientInfo(clientList)
//        }
    }

    private fun observeClientsListLiveData() {
        viewModel.observeClientList().observe(viewLifecycleOwner){
            clientViewAdapter.setClientInfo(it)
        }
    }

    private fun addingClient() {
        val clientName = arguments?.getString("clientName")
        if (clientName != null) {
            viewModel.addClientToTheList(clientName)
        }
    }


    private fun prepareRvForClientViewAdapter() {
        clientViewAdapter = ClientViewAdapter()
        binding.rvClientsForChat.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = clientViewAdapter
        }

    }


}