package com.example.bottomnavigation.models

data class Quotations(
    val contractorUserId : String?= null,
    val contractorName : String?= null,
    val quotationMessage : String? = null,
    val currentDate : String? = null,
    val contractorRating : String? = null,
    var isExpandable : Boolean=false,
    val quotationImage : String? = null
    )
