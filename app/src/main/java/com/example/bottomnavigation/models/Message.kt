package com.example.bottomnavigation.models

data class Message(
    val currentDate : String? = null,
    val currentTime : String? = null,
    val message: String?=null,
    val senderId: String?=null,
    val profileSharing : String? = null,
    val contractorName : String? = null,
)
