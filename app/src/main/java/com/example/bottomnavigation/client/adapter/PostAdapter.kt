package com.example.bottomnavigation.client.adapter
import android.content.Context
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.PostCardBinding
import com.example.bottomnavigation.databinding.PostItemViewClientSideBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PostAdapter(val context: Context):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var clientsPostLists = ArrayList<ClientPosts>()

    fun setPostList(clientsPostLists: ArrayList<ClientPosts>){
        this.clientsPostLists = clientsPostLists
        notifyDataSetChanged()
    }

        // binding should be val so that we can find it in onBindViewHolder
    class PostViewHolder (val binding: PostItemViewClientSideBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostItemViewClientSideBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val  data = clientsPostLists[position]
        holder.binding.apply {
                ivImg1.clipToOutline = true
                ivImg2.clipToOutline = true
                ivImg3.clipToOutline = true
                ivImg4.clipToOutline = true
        }
        holder.binding.tvTitle.text = data.name
        holder.binding.tvAddress.text = data.address
        holder.binding.workDesc.text = data.description
        holder.binding.tvDate.text = data.postTime
        holder.binding.apply {
            Glide.with(holder.itemView).load(data.imageUrls?.get(0)).into(ivImg1)
            Glide.with(holder.itemView).load(data.imageUrls?.get(1)).into(ivImg2)
            Glide.with(holder.itemView).load(data.imageUrls?.get(2)).into(ivImg3)
            Glide.with(holder.itemView).load(data.imageUrls?.get(3)).into(ivImg4)
        }
    }

    override fun getItemCount(): Int {
        return clientsPostLists.size
    }
}