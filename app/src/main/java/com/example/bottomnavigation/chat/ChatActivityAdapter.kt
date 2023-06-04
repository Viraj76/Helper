package com.example.bottomnavigation.chat

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bottomnavigation.R
import com.example.bottomnavigation.contractor.activity.ContractorProfileActivity
import com.example.bottomnavigation.databinding.ReceiveMessageBinding
import com.example.bottomnavigation.databinding.SendMessageBinding
import com.example.bottomnavigation.databinding.ShareProfileReceiveBinding
import com.example.bottomnavigation.databinding.ShareProfileSendBinding
import com.example.bottomnavigation.models.ContractorID

import com.example.bottomnavigation.models.Message
import com.example.bottomnavigation.models.RatedContractor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivityAdapter(val recyclerView: RecyclerView, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private  var messageList = ArrayList<Message>()
    private var SENT_MESSAGE = 1
    private var RECEIVED_MESSAGE = 2
    private var SHARE_PROFILE_SEND = 3
    private var SHARE_PROFILE_RECEIVE = 4

    fun setMessageList(messageList : ArrayList<Message>) {
        this.messageList = messageList
        notifyDataSetChanged()
        recyclerView.post {
            recyclerView.smoothScrollToPosition(itemCount - 1)
        }
    }

    class SendViewHolder(val binding: SendMessageBinding): ViewHolder(binding.root)
    class ReceiveViewHolder(val binding: ReceiveMessageBinding): ViewHolder(binding.root)
    class ShareProfileSend (val binding: ShareProfileSendBinding): ViewHolder(binding.root)
    class ShareProfileReceive(val binding: ShareProfileReceiveBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SENT_MESSAGE -> {
                SendViewHolder(SendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            RECEIVED_MESSAGE -> {
                ReceiveViewHolder(ReceiveMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            SHARE_PROFILE_SEND -> {
                ShareProfileSend(ShareProfileSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            SHARE_PROFILE_RECEIVE -> {
                ShareProfileReceive(ShareProfileReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        if(holder.javaClass == SendViewHolder::class.java){
            holder as SendViewHolder
            holder.binding.tvSendMessage.text = message.message
        }
        else if(holder.javaClass == ReceiveViewHolder::class.java){
            holder as ReceiveViewHolder
            holder.binding.tvReceiveMessage.text = message.message
        }
        else if (holder.javaClass == ShareProfileSend::class.java){
            holder as ShareProfileSend
            holder.binding.btnRateMe.text = message.message
            gettingContractorName(message.senderId!!,object : ContractorNameCallBack{
                override fun onGettingContractorName(name: String) {
                    holder.binding.profileNameTextView.text = name
                }

            })
        }
        else{
            holder as ShareProfileReceive
            holder.binding.btnRateMe.text = message.message
            holder.binding.btnRateMe.setOnClickListener {
                val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                val contractorId = message.senderId!!
                val ratedContractorRef = FirebaseDatabase.getInstance().getReference("Rated Contractor")
                    .child(contractorId)
                    .orderByChild("clientId")
                    .equalTo(currentUser)

                ratedContractorRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The client has already rated this contractor
                            Toast.makeText(context, "You have already rated this contractor.", Toast.LENGTH_SHORT).show()
                        } else {
                            // The client has not rated the contractor, allow rating
                            val builder = AlertDialog.Builder(context)
                            val customRateLayout = LayoutInflater.from(context).inflate(R.layout.card_view_rate, null)
                            builder.setView(customRateLayout)
                            builder.setCancelable(false)
                            val rateAlertDialogue = builder.create()
                            val submitButton = customRateLayout.findViewById<Button>(R.id.submitRatingButton)
                            val ratingBar = customRateLayout.findViewById<RatingBar>(R.id.contractorRatingBar)
                            var selectedRating = 0.0f
                            ratingBar.onRatingBarChangeListener =
                                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                                    selectedRating = rating
                                }
                            submitButton.setOnClickListener {
                                val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()
                                )
                                val ratedContractor = RatedContractor(currentUser, selectedRating.toString(),currentDate)
                                FirebaseDatabase.getInstance().getReference("Rated Contractor").child(contractorId)
                                    .push()
                                    .setValue(ratedContractor)
                                Toast.makeText(context, "Rated successfully", Toast.LENGTH_SHORT).show()
                                rateAlertDialogue.dismiss()
                            }
                            rateAlertDialogue.show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error
                    }
                })
            }
            gettingContractorName(message.senderId!!, object :ContractorNameCallBack{
                override fun onGettingContractorName(name: String) {
                    holder.binding.profileNameTextView.text = name
                }

            })
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    override fun getItemViewType(position: Int): Int {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val currentMessage = messageList[position]
        val isProfileSharing = currentMessage.profileSharing
        return if (isProfileSharing.equals("true")) {
            if (isProfileSharing != null) {
                Log.d("ss",isProfileSharing)
            }
            if (currentUser == currentMessage.senderId) SHARE_PROFILE_SEND
            else SHARE_PROFILE_RECEIVE
        } else {
            if (currentUser == currentMessage.senderId) SENT_MESSAGE
            else RECEIVED_MESSAGE
        }
    }

    private fun gettingContractorName(contractorId : String, callBack : ContractorNameCallBack){
        FirebaseDatabase.getInstance().getReference("Contractor Id's").child(contractorId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contractorDetails = snapshot.getValue(ContractorID::class.java)
                    callBack.onGettingContractorName(contractorDetails?.name!!)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
    interface ContractorNameCallBack{
        fun onGettingContractorName(name : String)
    }

    }


