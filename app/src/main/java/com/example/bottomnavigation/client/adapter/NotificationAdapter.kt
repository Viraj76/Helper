package com.example.bottomnavigation.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.client.fragments.NotificationFragment
import com.example.bottomnavigation.databinding.ItemViewNotificationBinding
import com.example.bottomnavigation.models.Quotations

class NotificationAdapter(val notificationFragment: NotificationFragment) : RecyclerView.Adapter<NotificationAdapter.QuotationsViewHolder>() {
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
        }
//        handling expandability
        val isExpandable:Boolean  = quotations.isExpandable
        holder.binding.apply {
            quotationTitle.visibility = if(isExpandable) View.VISIBLE else View.GONE
            tvQuotation.visibility = if(isExpandable) View.VISIBLE else View.GONE
            btnAccept.visibility = if(isExpandable) View.VISIBLE else View.GONE
            btnReject.visibility = if(isExpandable) View.VISIBLE else View.GONE

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
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }

    }
    override fun getItemCount(): Int {
        return quotationsList.size
    }
}