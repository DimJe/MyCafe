package com.Dimje.mymap

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var locationOverlay : LocationOverlay
    private lateinit var marker: Marker
    private lateinit var infoWindow: InfoWindow
    private var marker_list = mutableListOf<Marker>()
    var count_ediya :Int = 0
    var count_star :Int = 0
    var count_two :Int = 0
    var count_other :Int = 0
    val TAG:String = "로그"
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this){}
        var adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        val ediya = findViewById<Button>(R.id.ediya)
        ediya.setOnClickListener{
            Log.d(TAG,"이디야 () called")
            if(count_ediya%2 ==0){
                del_all()
                loadCafe("이디야")
                count_ediya++
                count_ediya = count_ediya%2
            }
            else{
                del(Color.BLUE)
                count_ediya++
            }
        }
        val two = findViewById<Button>(R.id.twosome)
        two.setOnClickListener{
            Log.d(TAG,"투썸 () called")
            if(count_two%2 ==0){
                del_all()
                loadCafe("투썸")
                count_two++
            }
            else{
                del()
                count_two++
            }
        }
        val star = findViewById<Button>(R.id.starbucks)
        star.setOnClickListener{
            Log.d(TAG,"스벜 () called")
            if(count_star%2 ==0){
                del_all()
                loadCafe("스타벅스")
                count_star++
            }
            else{
                del(Color.GREEN)
                count_star++
            }
        }
        val other = findViewById<Button>(R.id.other)
        other.setOnClickListener{
            Log.d(TAG,"그외 () called")
            if(count_other%2 ==0){
                del_all()
                loadCafe_other()
                count_other++
            }
            else{
                del(Color.GRAY)
                count_other++
            }
        }
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map_fragment, it).commit()
                }
        mapFragment.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        Log.d(TAG,"MainActivity - onCreate() end")

    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onMapReady(naverMap: NaverMap) {

        Log.d(TAG,"MainActivity - onMapReady() called")

        this.naverMap = naverMap
        naverMap.setLocationSource(locationSource)
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face)
        naverMap.addOnLocationChangeListener { location ->
//            Toast.makeText(this, "${location.latitude}, ${location.longitude}",
//                    Toast.LENGTH_SHORT).show()
            locationOverlay.position = LatLng(location.latitude, location.longitude)
            //Log.d(TAG,"onMapReady()-  ${locationOverlay.position.latitude}  ${locationOverlay.position.longitude}")
        }
        locationOverlay = naverMap.locationOverlay
        //loadCafe()
        var ui:UiSettings = naverMap.getUiSettings()
        ui.setLocationButtonEnabled(true)

    }
    fun loadCafe(name:String){

        val BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
        val REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"
        //Log.d(TAG," loadCafe()- ${locationOverlay.position.latitude}  ${locationOverlay.position.longitude}")
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_KAKAO_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(SearchCafeService::class.java)
        val callGetSearchCafe = api.getSearchCafe(REST_API_KEY, locationOverlay.position.longitude,locationOverlay.position.latitude,"CE7",3000,name)
        callGetSearchCafe.enqueue(object : Callback<Cafeinfo>{
            override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                Log.d(TAG,"MainActivity - onResponse() called")
                val result = response.body()
                //Log.d(TAG,"${result!!.documents[0].address_name}")
                showCafe(result)
            }
            override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                Log.d(TAG,"MainActivity - onFailure() called ${t.localizedMessage}")
            }
        })
    }
    fun loadCafe_other(){
        val BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
        val REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"
        //Log.d(TAG," loadCafe()- ${locationOverlay.position.latitude}  ${locationOverlay.position.longitude}")
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_KAKAO_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(SearchOtherService::class.java)
        val callGetSearchOther = api.getSearchOther(REST_API_KEY, locationOverlay.position.longitude,locationOverlay.position.latitude,"CE7",1000)
        callGetSearchOther.enqueue(object : Callback<Cafeinfo>{
            override fun onResponse(call: Call<Cafeinfo>, response: Response<Cafeinfo>) {
                Log.d(TAG,"MainActivity - onResponse() called")
                val result_other = response.body()
                result_other?.let {
                    for (Cafeinfo in it.documents){
                        //marker.map = null
                        marker  = Marker()
                        marker.icon = MarkerIcons.BLACK
                        marker.iconTintColor = Color.GRAY
                        infoWindow = InfoWindow()
                        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this@MainActivity) {
                            override fun getText(infoWindow: InfoWindow): CharSequence {
                                return Cafeinfo.place_name
                            }
                        }
                        marker.setOnClickListener {
                            var url :String = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query="+"${Cafeinfo.place_name}"
                            AlertDialog.Builder(this@MainActivity)
                                    .setTitle(Cafeinfo.place_name)
                                    .setMessage("${Cafeinfo.road_address_name}")
                                    .setPositiveButton("확인") { dialog, id ->
                                    }
                                    .setNegativeButton("이동하기") { dialog, id ->
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        startActivity(intent)
                                    }
                                    .show()
                            false
                        }
                        //Log.d(TAG,"MainActivity - showCafe() called${latLng.latitude},${latLng.longitude},${Cafeinfo.address}")
                        marker.position = LatLng(Cafeinfo.y.toDouble(),Cafeinfo.x.toDouble())
                        marker.map = naverMap
                        infoWindow.open(marker)
                        marker_list.add(marker)
                    }
                }
            }
            override fun onFailure(call: Call<Cafeinfo>, t: Throwable) {
                Log.d(TAG,"MainActivity - onFailure() called ${t.localizedMessage}")
            }
        })
    }
    fun showCafe(result : Cafeinfo?){
        result?.let {
            for (Cafeinfo in it.documents){
                //marker.map = null
                marker  = Marker()
                marker.icon = MarkerIcons.BLACK
                when(it.meta.same_name.keyword){
                    "이디야" -> marker.iconTintColor = Color.BLUE
                    "투썸" -> marker.iconTintColor = Color.argb(100,102,0,0)
                    "스타벅스" -> marker.iconTintColor = Color.GREEN
                }
                infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return Cafeinfo.place_name
                    }
                }
                marker.setOnClickListener {
                    var url :String = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query="+"${Cafeinfo.place_name}"
                    AlertDialog.Builder(this)
                            .setTitle(Cafeinfo.place_name)
                            .setMessage("${Cafeinfo.road_address_name}")
                            .setPositiveButton("확인") { dialog, id ->
                            }
                            .setNegativeButton("이동하기") { dialog, id ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
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
        for(i in marker_list){
            if(i.getIconTintColor()==color){
                i.map = null
                marker_list_del.add(i)

            }
        }
        Log.d(TAG,"321:MainActivity - del() called     ${marker_list_del.size}")
        Log.d(TAG,"322: ${marker_list.size}")
        marker_list.removeAll(marker_list_del)
        Log.d(TAG,"324: ${marker_list.size}")
    }
    fun del_all(){
        Log.d(TAG,"MainActivity - del_all() called              ${marker_list.size}")
        var marker_list_del = mutableListOf<Marker>()
        if (marker_list.isEmpty()) return
       when(marker_list[0].getIconTintColor()){
           Color.BLUE -> count_ediya++
           Color.GREEN -> count_star++
           Color.GRAY -> count_other++
           Color.argb(100,102,0,0) -> count_two++

       }
        for(i in marker_list){
            i.map = null
            marker_list_del.add(i)
        }
        Log.d(TAG,"322: ${marker_list.size}")
        marker_list.removeAll(marker_list_del)
        Log.d(TAG,"324: ${marker_list.size}")
    }


}