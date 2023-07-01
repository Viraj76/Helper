package com.example.bottomnavigation.client.adapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.Image
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.bottomnavigation.R
import com.example.bottomnavigation.contractor.activity.FullScreenActivity
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
        val imageUris = data.imageUrls
        if(imageUris.isNullOrEmpty()){
            holder.binding.ivImages.visibility = View.GONE
            holder.binding.tvNoImage.visibility = View.VISIBLE
        }
        else{
            holder.binding.ivImages.visibility = View.VISIBLE
            holder.binding.tvNoImage.visibility = View.GONE
            val imageViewList = listOf(holder.binding.ivImg1, holder.binding.ivImg2, holder.binding.ivImg3, holder.binding.ivImg4)
            for (i in 0 until minOf(imageUris.size, imageViewList.size)) {
                val currentImageView = imageViewList[i]
                Glide.with(holder.itemView)
                    .load(imageUris[i])
                    .override(800, 600)
                    .placeholder(R.drawable.loading_bar)
                    .into(currentImageView)
            }

        }
        holder.binding.ivImg1.setOnClickListener {
            val imageUri = data.imageUrls?.getOrNull(0)
            imageUri?.let {
                showFullScreenImageDialog(it)
            }
        }
        holder.binding.ivImg2.setOnClickListener {
            val imageUri = data.imageUrls?.getOrNull(1)
            imageUri?.let {
                showFullScreenImageDialog(it)
            }
        }
        holder.binding.ivImg3.setOnClickListener {
            val imageUri = data.imageUrls?.getOrNull(2)
            imageUri?.let {
                showFullScreenImageDialog(it)
            }
        }
        holder.binding.ivImg4.setOnClickListener {
            val imageUri = data.imageUrls?.getOrNull(3)
            imageUri?.let {
                showFullScreenImageDialog(it)
            }
        }
        holder.binding.apply {
            ivImg1.clipToOutline = true
            ivImg2.clipToOutline = true
            ivImg3.clipToOutline = true
            ivImg4.clipToOutline = true
            tvTitle.text = data.name
            workDesc.text = data.description
            tvAddress.text = data.address
            tvDate.text = data.postTime
            tvBudget.text = data.budget +"\\"+"-"
            tvSize.text = data.size
        }
    }
    override fun getItemCount(): Int {
        return clientsPostLists.size
    }
    private fun showFullScreenImageDialog(imageUri: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_full_screen)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val imageView = dialog.findViewById<ImageView>(R.id.imageViewFullScreen)
        Glide.with(context)
            .load(imageUri)
            .into(imageView)

        dialog.show()
    }
}