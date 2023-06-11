package com.example.bottomnavigation.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.databinding.ItemViewNotificationBinding
import com.example.bottomnavigation.models.Quotations

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.QuotationsViewHolder>() {
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
        holder.binding.apply {
            tvContractorName.text = quotations.contractorName
            tvAverageRating.text = quotations.contractorRating
            tvDate.text = quotations.currentDate
            tvQuotation.text = quotations.quotationMessage
        }
    }

    override fun getItemCount(): Int {
        return quotationsList.size
    }
}