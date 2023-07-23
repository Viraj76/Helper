package com.example.bottomnavigation.client

import android.widget.Filter
import com.example.bottomnavigation.client.adapter.ContractorsAdapter
import com.example.bottomnavigation.models.ContractorID
import java.util.*
import kotlin.collections.ArrayList

class FilteringContractors(
    private val adapter : ContractorsAdapter,
    private val filterContractors : ArrayList<ContractorID>
) : Filter(){
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()
        if(!constraint.isNullOrEmpty()){
            val query = constraint.toString().trim().uppercase(Locale.getDefault())
            val keyword = query.split(" ")
            val filteredContractors = kotlin.collections.ArrayList<ContractorID>()

            for(contractor in filterContractors){
                if(keyword.any{ keyword->
                    contractor.address?.uppercase(Locale.getDefault())?.contains(keyword)==true||
                    contractor.businessType?.uppercase(Locale.getDefault())?.contains(keyword)==true
                    }){
                    filteredContractors.add(contractor)
                }
            }
            results.count = filteredContractors.size
            results.values = filteredContractors
        }else{
            results.count = filterContractors.size
            results.values = filterContractors
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapter.contractorsList = results.values as ArrayList<ContractorID>
        adapter.notifyDataSetChanged()
    }

}