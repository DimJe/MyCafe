package com.Dimje.mymap.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.locationOverlay
import com.Dimje.mymap.SearchCafeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIViewModel : ViewModel(){

    var result : MutableLiveData<Cafeinfo> = MutableLiveData()
    var retrofit : Retrofit
    var BASE_URL_KAKAO_API : String
    var REST_API_KEY : String
    init {
        Log.d(TAG, "object: created")
        BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
        REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_KAKAO_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    


}