package com.Dimje.mymap

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.Dimje.mymap.API.CallAPI
import com.Dimje.mymap.RecyclerView.CafeListActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*  1.api 모든 결과 불러오기
    2.버튼 꾸미기
    3.text classification 구현해보기
    4.splash 구현 o
    5.소스파일 나눠보기
    6.firebase 연동
    7.코드 암호화
*/
class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG:String = "로그"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lateinit var intent: Intent

        show_with_list.setOnClickListener {
            intent = Intent(this,CafeListActivity::class.java)
            startActivity(intent)
        }
        show_with_map.setOnClickListener {
            intent = Intent(this,CafeMapActivity::class.java)
            startActivity(intent)
        }
        Log.d(TAG,"MainActivity - onCreate() end")

    }




}