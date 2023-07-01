package com.example.bottomnavigation.client.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bottomnavigation.R
import com.example.bottomnavigation.client.fragments.NotificationFragment
import com.example.bottomnavigation.databinding.ItemViewNotificationBinding
import com.example.bottomnavigation.models.Quotations

class NotificationAdapter(
    val notificationFragment: Context,
    private val onRejectButtonClick:((Quotations) -> Unit)? = null,
    private val onAcceptButtonClick:((Quotations) -> Unit)? = null
) : RecyclerView.Adapter<NotificationAdapter.QuotationsViewHolder>() {
    private var quotationsList = ArrayList<Quotations>()
    fun setQuotationsLists(quotations : ArrayList<Quotations>){
        this.quotationsList = quotations
        notifyDataSetChanged()
    }
    class QuotationsViewHolder(val binding : ItemViewNotificationBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotationsViewHolder {
        return QuotationsViewHolder(ItemViewNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: QuotationsViewHolder, position: Int) {
        val quotations = quotationsList[position]
        //setting data
        holder.binding.apply {
            tvContractorName.text = quotations.contractorName
            tvAverageRating.text = quotations.contractorRating
            tvDate.text = quotations.currentDate
            tvQuotation.text = quotations.quotationMessage
            if(quotations.quotationImage != ""){
                Glide.with(holder.itemView).load(quotations.quotationImage).into(ivQuotation)
                holder.binding.ivQuotation.setOnClickListener {
                    val imageUri = quotations.quotationImage
                    imageUri?.let {
                        showFullScreenImageDialog(it)
                    }
                }
            }
            btnReject.setOnClickListener { onRejectButtonClick?.invoke(quotations) }
            btnAccept.setOnClickListener { onAcceptButtonClick?.invoke(quotations) }
        }
//        handling expandability
        val isExpandable:Boolean  = quotations.isExpandable
        holder.binding.apply {
            quotationTitle.visibility = if(isExpandable) View.VISIBLE else View.GONE
            tvQuotation.visibility = if(isExpandable) View.VISIBLE else View.GONE
            btnAccept.visibility = if(isExpandable) View.VISIBLE else View.GONE
            btnReject.visibility = if(isExpandable) View.VISIBLE else View.GONE
            ivQuotation.visibility = if(isExpandable) View.VISIBLE else View.GONE

            visibleConstraintLayout.setOnClickListener {
                isAnyItemExpanded(position)
                quotations.isExpandable = !quotations.isExpandable
                notifyItemChanged(position,Unit)
            }
        }
    }
    private fun isAnyItemExpanded(position: Int) {
        val temp = quotationsList.indexOfFirst { it.isExpandable }
        if (temp >= 0 && temp != position) {
            quotationsList[temp].isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }
    override fun onBindViewHolder(holder: QuotationsViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.binding.apply {
                quotationTitle.visibility = View.GONE
                tvQuotation.visibility = View.GONE
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
                ivQuotation.visibility = View.GONE
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }

    }
    override fun getItemCount(): Int {
        return quotationsList.size
    }
    private fun showFullScreenImageDialog(imageUri: String) {
        val dialog = Dialog(notificationFragment)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_full_screen)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val imageView = dialog.findViewById<ImageView>(R.id.imageViewFullScreen)
        Glide.with(notificationFragment)
            .load(imageUri)
            .into(imageView)

        dialog.show()
    }
}