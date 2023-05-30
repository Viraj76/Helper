package com.example.bottomnavigation.contractor.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.ChatActivity
import com.example.bottomnavigation.R
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.PostCardContractorBinding
import com.example.bottomnavigation.databinding.PostItemViewClientSideBinding
import com.example.bottomnavigation.databinding.PostItemViewContractorSideBinding

class ContractorPostAdapter(val context: Context):RecyclerView.Adapter<ContractorPostAdapter.ContractorPostViewHolder>() {
    private  var contractorPostList=ArrayList<ClientPosts>()

    fun setPosts(contractorSidePostList : ArrayList<ClientPosts>){
        this.contractorPostList = contractorSidePostList
        notifyDataSetChanged()
    }

    class ContractorPostViewHolder( val binding:PostItemViewContractorSideBinding):ViewHolder(binding.root){
     
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractorPostViewHolder {
        return  ContractorPostViewHolder(PostItemViewContractorSideBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ContractorPostViewHolder, position: Int) {

        val  data = contractorPostList[position]
        holder.binding.tvTitle.text = data.name
        holder.binding.tvAddress.text = data.address
        holder.binding.workDesc.text = data.description

        holder.binding.ivFav.setOnClickListener {
            Toast.makeText(context,"Soon!",Toast.LENGTH_SHORT).show()
        }
        holder.binding.ivMessage.setOnClickListener {
//
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",data.name)
            intent.putExtra("id",data.userId)
            context.startActivity(intent)
//            oonChatOptionClicked(data.name!!,data.userId!!)
////            val bundle = Bundle()
////            bundle.putString("clientName",data.name)
////            Navigation.findNavController(it!!).navigate(R.id.action_homeFragment3_to_contractorChatFragment,bundle)
//
        }
    }

    override fun getItemCount(): Int {
       return contractorPostList.size
    }


}