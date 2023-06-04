package com.example.bottomnavigation.contractor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.databinding.ItemViewRatingBinding
import com.example.bottomnavigation.models.ClientWhoRated
import com.example.bottomnavigation.models.RatedContractor

class RatingHistoryAdapter:RecyclerView.Adapter<RatingHistoryAdapter.ClientWhoRatedViewHolder>() {

    var clientWhoRatedList = ArrayList<RatedContractor>()
    fun setClientWhoRated(clientList: ArrayList<RatedContractor>){
        this.clientWhoRatedList = clientList
       notifyDataSetChanged()
    }
    class ClientWhoRatedViewHolder(val binding : ItemViewRatingBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientWhoRatedViewHolder {
        return ClientWhoRatedViewHolder(ItemViewRatingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ClientWhoRatedViewHolder, position: Int) {
        val clientData = clientWhoRatedList[position]
        holder.binding.apply {
            clientName.text = clientData.clientId?.substring(0,5)
            ratedDate.text = clientData.rateDate
            ratingStars.rating = clientData.rating?.toFloat()!!
        }
    }

    override fun getItemCount(): Int {
        return clientWhoRatedList.size
    }
}