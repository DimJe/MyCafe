package com.Dimje.mymap.UI.dialog

interface DialogListener {
    fun onSubmitClick(id : Int,review:String,point:Double)
    fun onSearchClick(id : Int,type:String)
}