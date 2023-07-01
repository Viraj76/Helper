package com.example.bottomnavigation.contractor.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bottomnavigation.R
import com.example.bottomnavigation.chat.ChatActivity
import com.example.bottomnavigation.contractor.activity.QuestionsActivity
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.PostItemViewContractorSideBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContractorPostAdapter(val context: Context):RecyclerView.Adapter<ContractorPostAdapter.ContractorPostViewHolder>() {
    private  var contractorPostList=ArrayList<ClientPosts>()

    fun setPosts(contractorSidePostList : ArrayList<ClientPosts>){
        this.contractorPostList = contractorSidePostList
        notifyDataSetChanged()
    }

    class ContractorPostViewHolder( val binding:PostItemViewContractorSideBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractorPostViewHolder {
        return  ContractorPostViewHolder(PostItemViewContractorSideBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ContractorPostViewHolder, position: Int) {
        val  data = contractorPostList[position]
        val imageUris = data.imageUrls
        if(imageUris.isNullOrEmpty()){
            holder.binding.apply {
                ivImages.visibility = View.GONE
                tvNoImage.visibility = View.VISIBLE
            }
        }
        else{
            holder.binding.apply {
                ivImages.visibility = View.VISIBLE
                tvNoImage.visibility = View.GONE
            }
            val imageViewList = listOf(holder.binding.ivImg1, holder.binding.ivImg2, holder.binding.ivImg3, holder.binding.ivImg4)
            for (i in 0 until minOf(imageUris.size, imageViewList.size)) {
                val currentImageView = imageViewList[i]
                Glide.with(holder.itemView)
                    .load(imageUris[i])
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
            ivFav.setOnClickListener { Toast.makeText(context, "Soon!", Toast.LENGTH_SHORT).show() }
            ivMessage.setOnClickListener {
                val clientId = data.userId
                val contractorId = FirebaseAuth.getInstance().currentUser?.uid
                val chatRoomId = contractorId + clientId
                Log.d("cc", chatRoomId)
                val chatbaseRef = FirebaseDatabase.getInstance().getReference("Quotations")
                chatbaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val roomIdExists = snapshot.hasChild(chatRoomId)  //checking if the child present
                        if (roomIdExists) {
                            Toast.makeText(context,"Client will contact you!",Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(context, QuestionsActivity::class.java)
                            intent.putExtra("id", data.userId)
                            context.startActivity(intent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error if the operation is canceled
                    }
                })
            }

        }
    }

    override fun getItemCount(): Int {
       return contractorPostList.size
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