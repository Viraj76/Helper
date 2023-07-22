package com.example.bottomnavigation.models


data class ClientPosts(
    val userId: String ?=null,
    val name : String?=null,
    val category : String?=null,
    val budget : String?=null,
    val size: String?=null,
    val address : String ?=null,
    val description: String?=null,
    val postTime : String ?= null,
    val imageUrls: List<String>?=null
)
