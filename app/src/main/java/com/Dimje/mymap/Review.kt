package com.Dimje.mymap

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Review(
    val review : String="",
    val taste : String="",
    val beauty : String="",
    val study : String="",
    val date:String=""
)