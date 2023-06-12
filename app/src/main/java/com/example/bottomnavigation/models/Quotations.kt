package com.example.bottomnavigation.models

data class Quotations(
    val contractorName : String?= null,
    val quotationMessage : String? = null,
    val currentDate : String? = null,
    val contractorRating : String? = null,
    var isExpandable : Boolean=false,

    )
