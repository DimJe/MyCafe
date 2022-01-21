package com.Dimje.mymap

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.Dimje.mymap.API.CallAPI
import com.Dimje.mymap.RecyclerView.CafeListActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_main.*

/*  1.api 모든 결과 불러오기
    2.버튼 꾸미기 o
    3.text classification 구현해보기
    4.splash 구현 o
    5.소스파일 나눠보기 o
    6.firebase 연동 o
    7.코드 암호화
    8.평가등록 구현
    9.평가 불러오기 구현
*/
class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    companion object{
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        val callApi : CallAPI = CallAPI()
        lateinit var locationOverlay : LocationOverlay
        const val TAG: String = "로그"
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var marker: Marker
    private lateinit var infoWindow: InfoWindow
    private lateinit var naverMap: NaverMap
    private var markerList = mutableListOf<Marker>()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map_with_brand.setOnClickListener {

            val intent = Intent(this,CafeBrandCheckBox::class.java)
            startActivity(intent)
        }

        look_as_list.setOnClickListener {
            val intent = Intent(this,CafeListActivity::class.java)
            startActivity(intent)
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
    override fun onMapReady(NaverMap: NaverMap) {

        Log.d(TAG,"MainActivity - onMapReady() called")

        naverMap = NaverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.addOnLocationChangeListener { location ->
            locationOverlay.position = LatLng(location.latitude, location.longitude)
        }
        locationOverlay = naverMap.locationOverlay
        var ui: UiSettings = naverMap.getUiSettings()
        ui.isLocationButtonEnabled = true
        CallAPI.result.observe(this, Observer {
            Log.d(TAG, "onCreate - observe")
            del_all()
            showCafe(CallAPI.result.value)
        })

    }
    private fun showCafe(result: Cafeinfo?, color: Int = Color.GRAY){
        Log.d(TAG, "showCafe: called")
        result?.let {
            for (cafeInfo in it.documents){
                Log.d(TAG, "showCafe: ${cafeInfo.address_name}")
                marker  = Marker()
                marker.icon = MarkerIcons.BLACK
                when(it.meta.same_name){
                    null -> marker.iconTintColor = color
                    else ->{
                        when(it.meta.same_name.keyword){
                            "이디야"-> marker.iconTintColor = Color.BLUE
                            "투썸" -> marker.iconTintColor = Color.argb(100,102,0,0)
                            "스타벅스" -> marker.iconTintColor = Color.GREEN
                        }
                    }

                }
                infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return cafeInfo.place_name
                    }
                }
                marker.setOnClickListener {
                    var url :String = "https://search.naver.com/search.naver?where=nexearch&sm=top_sly.hst&fbm=0&acr=1&ie=utf8&query="+"${cafeInfo.place_name}"
                    AlertDialog.Builder(this)
                        .setTitle(cafeInfo.place_name)
                        .setMessage("${cafeInfo.road_address_name}")
                        .setPositiveButton("확인") { dialog, id ->
                        }
                        .setNegativeButton("이동하기") { dialog, id ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                        .show()
                    false
                }
                marker.position = LatLng(cafeInfo.y.toDouble(),cafeInfo.x.toDouble())
                marker.map = naverMap
                markerList.add(marker)
                infoWindow.open(marker)
            }
        }
    }

    fun del_all(){
        Log.d(TAG,"CallAPI - del_all() called   ${markerList.size}")
        var markerListDel = mutableListOf<Marker>()
        if (markerList.isEmpty()) return
        markerList.forEach {
            it.map = null
            markerListDel.add(it)
        }
        Log.d(TAG,"before_array_size : ${markerList.size}")
        markerList.removeAll(markerListDel)
        Log.d(TAG,"after_array_size : ${markerList.size}")
    }
}




