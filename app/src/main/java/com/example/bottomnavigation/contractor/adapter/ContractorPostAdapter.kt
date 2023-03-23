package com.example.bottomnavigation.contractor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.PostCardContractorBinding

class ContractorPostAdapter:RecyclerView.Adapter<ContractorPostAdapter.ContractorPostViewHolder>() {
    private  var contractorPostList=ArrayList<ClientPosts>()

    fun setPosts(contractorSidePostList : ArrayList<ClientPosts>){
        this.contractorPostList = contractorSidePostList
        notifyDataSetChanged()
    }

    class ContractorPostViewHolder( val binding:PostCardContractorBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractorPostViewHolder {
        return  ContractorPostViewHolder(PostCardContractorBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ContractorPostViewHolder, position: Int) {

        val  data = contractorPostList[position]
        holder.binding.tvName.text = data.name
        holder.binding.tvAddress.text = data.address
        holder.binding.tvDescription.text = data.description

    }

    override fun getItemCount(): Int {
       return contractorPostList.size
    }


}