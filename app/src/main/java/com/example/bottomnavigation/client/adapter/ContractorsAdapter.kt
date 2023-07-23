package com.example.bottomnavigation.client.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigation.client.FilteringContractors
import com.example.bottomnavigation.databinding.ContractorsItemViewBinding
import com.example.bottomnavigation.models.ContractorID

class ContractorsAdapter : RecyclerView.Adapter<ContractorsAdapter.ContractorsViewHolder>(), Filterable {

    class ContractorsViewHolder(val binding: ContractorsItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

     var contractorsList = ArrayList<ContractorID>()
    private var filterList =  ArrayList<ContractorID>()
    private var filter : FilteringContractors? = null

    fun setContractorList(contractorList: ArrayList<ContractorID>) {
        this.contractorsList = contractorList
        this.filterList = ArrayList(contractorList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractorsViewHolder {
        return ContractorsViewHolder(
            ContractorsItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContractorsViewHolder, position: Int) {
        val contractorsDetails = contractorsList[position]
        holder.binding.apply {
            contractorName.text = contractorsDetails.name
            contractorAddress.text = contractorsDetails.address
            contractorBusiness.text = contractorsDetails.businessType
            contractorNumber.text = contractorsDetails.phoneNumber
            contactorEmail.text = contractorsDetails.email

            // Set click listeners for email and phone number TextViews
            contactorEmail.setOnClickListener {
                contractorsDetails.email?.let { it1 -> copyToClipboard(it.context, it1, "Email") }
            }

            contractorNumber.setOnClickListener {
                contractorsDetails.phoneNumber?.let { it1 ->
                    copyToClipboard(it.context,
                        it1, "Phone Number")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return contractorsList.size
    }

    // Function to copy text to clipboard
    private fun copyToClipboard(context: Context, text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        // Vibrate the device for a short duration (milliseconds)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Newer devices with Android Oreo and above
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Older devices
            vibrator.vibrate(50)
        }

        val toastMessage = "$label copied to clipboard"
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilteringContractors(this,filterList)

        }
        return filter as FilteringContractors
    }
}
