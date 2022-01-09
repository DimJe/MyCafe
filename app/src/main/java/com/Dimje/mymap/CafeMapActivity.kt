package com.Dimje.mymap

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.Dimje.mymap.API.CallAPI
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_cafe_map.*


// API 요청 후 응답 메소드에서 현재 실행중인 액티비티 얻은 후 종료 시켜서 CafeMapActivity - onResume()에서 지도에 띄워야함
class CafeMapActivity : AppCompatActivity(),OnMapReadyCallback {
    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        lateinit var naverMap: NaverMap
        lateinit var callAPI: CallAPI
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationOverlay : LocationOverlay
    private lateinit var marker: Marker
    private lateinit var infoWindow: InfoWindow
    private var marker_list = mutableListOf<Marker>()
    var check : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"CafeMapActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_map)

        map_with_brand.setOnClickListener {

            val intent = Intent(this,CafeBrandCheckBox::class.java)
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
        Log.d(TAG,"CafeMapActivity - onCreate() end")

    }

    override fun onPause() {
        super.onPause()
        check = true
        Log.d(TAG, "CafeMapActivity - onPause: called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CafeMapActivity - onResume: called")
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

        Log.d(TAG,"CafeMapActivity - onMapReady() called")

        naverMap = NaverMap
        naverMap.setLocationSource(locationSource)
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow)
        naverMap.addOnLocationChangeListener { location ->
            locationOverlay.position = LatLng(location.latitude, location.longitude)
        }
        locationOverlay = naverMap.locationOverlay
        var ui: UiSettings = naverMap.getUiSettings()
        ui.setLocationButtonEnabled(true)
        callAPI = CallAPI(locationOverlay)

    }
    fun showCafe(result: Cafeinfo?, color: Int = Color.GRAY){
        Log.d(TAG, "showCafe: called")
        if(result==null) Log.d(MainActivity.TAG, "showCafe: null")
        result?.let {
            for (Cafeinfo in it.documents){
                Log.d(TAG, "showCafe: ${Cafeinfo.address_name}")
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
                marker.position = LatLng(Cafeinfo.y.toDouble(),Cafeinfo.x.toDouble())
                marker.map = naverMap
                marker_list.add(marker)
                infoWindow.open(marker)
            }
        }
    }

    fun del_all(){
        Log.d(TAG,"CallAPI - del_all() called   ${marker_list.size}")
        var marker_list_del = mutableListOf<Marker>()
        if (marker_list.isEmpty()) return
        marker_list.forEach {
            it.map = null
            marker_list_del.add(it)
        }
        Log.d(TAG,"before_array_size : ${marker_list.size}")
        marker_list.removeAll(marker_list_del)
        Log.d(TAG,"after_array_size : ${marker_list.size}")
    }
}