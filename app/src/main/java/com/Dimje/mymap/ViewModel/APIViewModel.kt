package com.Dimje.mymap.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.locationOverlay
import com.Dimje.mymap.SearchCafeService
import com.Dimje.mymap.SearchOtherService
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

    fun loadCafe(name:String) {
        val api = retrofit.create(SearchCafeService::class.java)
        val callGetSearchCafe = api.getSearchCafe(REST_API_KEY,
            locationOverlay.position.longitude,
            locationOverlay.position.latitude,"CE7",2100,name)
        callGetSearchCafe.enqueue(object : Callback<Cafeinfo> {
            override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                Log.d(TAG,"CallAPI - onResponse() called")
                result.value = response.body()
            }
            override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                Log.d(TAG,"CallAPI - onFailure() called ${t.localizedMessage}")
            }
        })
    }
    fun loadCafe_other(){
        val api = retrofit.create(SearchOtherService::class.java)
        val callGetSearchOther = api.getSearchOther(REST_API_KEY,
            locationOverlay.position.longitude,
            locationOverlay.position.latitude,"CE7",1000)
        callGetSearchOther.enqueue(object : Callback<Cafeinfo> {
            override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                Log.d(TAG,"MainActivity - onResponse() called")
                result.value = response.body()
            }
            override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                Log.d(TAG,"MainActivity - onFailure() called ${t.localizedMessage}")
            }
        })
    }


}