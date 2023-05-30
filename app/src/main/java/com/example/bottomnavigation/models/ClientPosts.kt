package com.example.bottomnavigation.models

import android.net.Uri

data class ClientPosts(
    val userId: String ?=null,
    val name : String?=null,
    val address : String ?=null,
    val description: String?=null,
    val postTime : String ?= null,
    val imageUrls: List<String>?=null
)
