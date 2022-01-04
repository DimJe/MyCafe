package com.Dimje.mymap.API

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.MainActivity
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.naverMap
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

class CallAPI(var locationOverlay: LocationOverlay,val context: Context) {
    private lateinit var marker: Marker
    private lateinit var infoWindow: InfoWindow
    private var marker_list = mutableListOf<Marker>()
    var result : Cafeinfo? = null
    val BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
    val REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_KAKAO_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun loadCafe(name:String){
        val api = retrofit.create(SearchCafeService::class.java)
        val callGetSearchCafe = api.getSearchCafe(REST_API_KEY,
            locationOverlay.position.longitude,locationOverlay.position.latitude,"CE7",3000,name)
        callGetSearchCafe.enqueue(object : Callback<Cafeinfo> {
            override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                Log.d(MainActivity.TAG,"MainActivity - onResponse() called")
                result = response.body()
                showCafe(result)
            }
            override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                Log.d(MainActivity.TAG,"MainActivity - onFailure() called ${t.localizedMessage}")
            }
        })
    }
    fun loadCafe_other(){
        val api = retrofit.create(SearchOtherService::class.java)
        val callGetSearchOther = api.getSearchOther(REST_API_KEY,
            locationOverlay.position.longitude,locationOverlay.position.latitude,"CE7",1000)
        callGetSearchOther.enqueue(object : Callback<Cafeinfo>{
            override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                Log.d(MainActivity.TAG,"MainActivity - onResponse() called")
                result = response.body()
                showCafe(result)
            }
            override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                Log.d(MainActivity.TAG,"MainActivity - onFailure() called ${t.localizedMessage}")
            }
        })
    }
    fun showCafe(result : Cafeinfo?){
        Log.d(MainActivity.TAG, "showCafe: called")
        if(result==null) Log.d(MainActivity.TAG, "showCafe: null")
        result?.let {
            for (Cafeinfo in it.documents){
                Log.d(MainActivity.TAG, "showCafe: ${Cafeinfo.address_name}")
                //marker.map = null
                marker  = Marker()
                marker.icon = MarkerIcons.BLACK
                when(it.meta.same_name){
                    null -> marker.iconTintColor = Color.GRAY
                    else ->{
                        when(it.meta.same_name.keyword){
                            "이디야"-> marker.iconTintColor = Color.BLUE
                            "투썸" -> marker.iconTintColor = Color.argb(100,102,0,0)
                            "스타벅스" -> marker.iconTintColor = Color.GREEN
                        }
                    }

                }
                infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(context) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return Cafeinfo.place_name
                    }
                }
                marker.setOnClickListener {
                    var url :String = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query="+"${Cafeinfo.place_name}"
                    AlertDialog.Builder(context)
                        .setTitle(Cafeinfo.place_name)
                        .setMessage("${Cafeinfo.road_address_name}")
                        .setPositiveButton("확인") { dialog, id ->
                        }
                        .setNegativeButton("이동하기") { dialog, id ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                        .show()
                    false
                }
                //Log.d(TAG,"MainActivity - showCafe() called${latLng.latitude},${latLng.longitude},${Cafeinfo.address}")
                marker.position = LatLng(Cafeinfo.y.toDouble(),Cafeinfo.x.toDouble())
                marker.map = naverMap
                marker_list.add(marker)
                infoWindow.open(marker)
            }
        }
    }
    fun del(color:Int = Color.argb(100,102,0,0)){
        var marker_list_del = mutableListOf<Marker>()
        marker_list.forEach {
            if(it.iconTintColor ==color){
                it.map = null
                marker_list_del.add(it)

            }
        }
        Log.d(MainActivity.TAG,"321:MainActivity - del() called     ${marker_list_del.size}")
        Log.d(MainActivity.TAG,"322: ${marker_list.size}")
        marker_list.removeAll(marker_list_del)
        Log.d(MainActivity.TAG,"324: ${marker_list.size}")
    }
    fun del_all(){
        Log.d(MainActivity.TAG,"MainActivity - del_all() called   ${marker_list.size}")
        var marker_list_del = mutableListOf<Marker>()
        if (marker_list.isEmpty()) return
//        when(marker_list[0].getIconTintColor()){
//            Color.BLUE -> count_ediya++
//            Color.GREEN -> count_star++
//            Color.GRAY -> count_other++
//            Color.argb(100,102,0,0) -> count_two++
//
//        }
        marker_list.forEach {
            it.map = null
            marker_list_del.add(it)
        }
        Log.d(MainActivity.TAG,"322: ${marker_list.size}")
        marker_list.removeAll(marker_list_del)
        Log.d(MainActivity.TAG,"324: ${marker_list.size}")
    }
}