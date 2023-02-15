package com.Dimje.mymap

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Review{
    var review : String = ""
    var point : Double = 0.0
    var date:String = ""

    constructor(){}
    constructor(review: String,point: Double,date: String){
        this.review = review
        this.point = point
        this.date = date
    }

}