package com.Dimje.mymap.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.locationOverlay
import com.Dimje.mymap.Repository.RemoteRepository
import com.Dimje.mymap.SearchCafeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIViewModel(val remoteRepository: RemoteRepository) : ViewModel(){

    var result : MutableLiveData<Cafeinfo> = MutableLiveData()


    


}