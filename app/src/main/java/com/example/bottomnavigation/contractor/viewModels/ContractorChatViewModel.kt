package com.example.bottomnavigation.contractor.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContractorChatViewModel:ViewModel() {

    private var clientList = MutableLiveData<String>()

    fun addClientToTheList(clientName: String){
        clientList.postValue(clientName)
    }

    fun observeClientList():MutableLiveData<String>{
        return clientList
    }
}