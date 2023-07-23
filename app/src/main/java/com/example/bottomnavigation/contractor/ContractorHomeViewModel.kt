package com.example.bottomnavigation.contractor

import androidx.lifecycle.ViewModel
import com.example.bottomnavigation.models.ClientPosts

class ContractorHomeViewModel : ViewModel() {
    var retrievedPosts: ArrayList<ClientPosts> = arrayListOf()
}
