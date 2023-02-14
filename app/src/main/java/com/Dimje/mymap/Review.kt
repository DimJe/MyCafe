package com.Dimje.mymap

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Review(
    var review : String?="",
    var point : Float? = null,
    var date:String?=""
)