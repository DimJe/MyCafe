package com.Dimje.mymap.API

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.MainActivity
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.CafeMapActivity.Companion.naverMap
import com.Dimje.mymap.SearchCafeService
import com.Dimje.mymap.SearchOtherService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CallAPI(var locationOverlay: LocationOverlay) {
    companion object{
        var result : MutableLiveData<Cafeinfo> = MutableLiveData()
    }
    val BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
    val REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_KAKAO_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun loadCafe(name:String) {
        Log.d(TAG, "loadCafe: called")
        val api = retrofit.create(SearchCafeService::class.java)
        val callGetSearchCafe = api.getSearchCafe(REST_API_KEY,
            locationOverlay.position.longitude,locationOverlay.position.latitude,"CE7",3000,name)
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
            locationOverlay.position.longitude,locationOverlay.position.latitude,"CE7",1000)
        callGetSearchOther.enqueue(object : Callback<Cafeinfo>{
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