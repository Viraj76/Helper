package com.example.bottomnavigation.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.models.ClientPosts
import com.example.bottomnavigation.databinding.HistoryCardBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter:RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private  var clientPostHistoryList = ArrayList<ClientPosts>()

    fun setClientHistory(clientHistory: ArrayList<ClientPosts>){
        this.clientPostHistoryList= clientHistory
        notifyDataSetChanged()
    }

    class HistoryViewHolder(val binding:HistoryCardBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(HistoryCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyy")
        val current = LocalDateTime.now().format(formatter)
        val data = clientPostHistoryList[position]
        holder.binding.tvName.text = data.name.toString()
        holder.binding.requirements.text = data.description.toString()
        holder.binding.tvDate.text = current

    }

    override fun getItemCount(): Int {
        return clientPostHistoryList.size
    }


}