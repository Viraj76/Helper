package com.example.bottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.databinding.ClientChatItemViewBinding
import com.example.bottomnavigation.databinding.FragmentContractorChatBinding
import com.example.bottomnavigation.models.ClientID
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClientViewAdapter() : RecyclerView.Adapter<ClientViewAdapter.ClientViewHolder>() {
    class ClientViewHolder(val binding:ClientChatItemViewBinding):ViewHolder(binding.root)


    private var clientList: ArrayList<String> = arrayListOf()

    fun setClientInfo(clientInfo: String) {
        clientList.add(clientInfo)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        return  ClientViewHolder(ClientChatItemViewBinding.inflate(LayoutInflater.from(parent.context) ,parent, false))

    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {

        holder.binding.tvClientName.text = clientList[position]
    }

    override fun getItemCount(): Int {
        return clientList.size
    }



}
