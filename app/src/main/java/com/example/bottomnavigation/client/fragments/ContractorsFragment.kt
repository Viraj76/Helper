package com.example.bottomnavigation.client.fragments

import ContractorsViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigation.R
import com.example.bottomnavigation.client.adapter.ContractorsAdapter
import com.example.bottomnavigation.databinding.FragmentContractorsBinding
import com.example.bottomnavigation.models.ContractorID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContractorsFragment : Fragment() {
    private lateinit var binding: FragmentContractorsBinding
    private lateinit var contractorsAdapter: ContractorsAdapter
    private val viewModel: ContractorsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContractorsBinding.inflate(layoutInflater)

        prepareRvForContractorAdapter()
        setupSearch()

        return binding.root
    }

    private fun prepareRvForContractorAdapter() {
        contractorsAdapter = ContractorsAdapter()
        binding.rvContractors.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contractorsAdapter
        }
    }

    private fun setupSearch() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                contractorsAdapter.filter.filter(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.contractorList.isEmpty()) {
            // Fetch data from Firebase only if the contractorList is empty
            showingAllContractors()
        } else {
            // If the contractorList is not empty, set the list to the adapter
            contractorsAdapter.setContractorList(viewModel.contractorList)
        }
    }

    private fun showingAllContractors() {
        FirebaseDatabase.getInstance().getReference("Contractor Id's")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModel.contractorList.clear() // Clear the list before adding new data
                    for (allDetails in snapshot.children) {
                        val detail = allDetails.getValue(ContractorID::class.java)
                        detail?.let { viewModel.contractorList.add(it) }
                    }
                    contractorsAdapter.setContractorList(viewModel.contractorList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle Firebase error, if needed
                }
            })
    }
}
