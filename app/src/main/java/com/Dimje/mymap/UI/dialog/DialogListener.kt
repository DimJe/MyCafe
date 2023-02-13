package com.Dimje.mymap.UI.dialog

interface DialogListener {
    fun onSubmitClick(id : Int,review:String,point:Float)
    fun onSearchClick(id : Int,type:String)
}