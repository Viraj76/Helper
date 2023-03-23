package com.example.bottomnavigation.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.PostCardBinding

class PostAdapter:RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var clientsPostLists = ArrayList<ClientPosts>()

    fun setPostList(clientsPostLists: ArrayList<ClientPosts>){
        this.clientsPostLists = clientsPostLists
        notifyDataSetChanged()
    }

        // binding should be val so that we can find it in onBindViewHolder
    class PostViewHolder (val binding: PostCardBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val  data = clientsPostLists[position]
        holder.binding.tvName.text = data.name
        holder.binding.tvAddress.text = data.address
        holder.binding.tvDescription.text = data.description
    }

    override fun getItemCount(): Int {
        return clientsPostLists.size
    }
}