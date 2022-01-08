package com.Dimje.mymap

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.Dimje.mymap.API.CallAPI
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.Dimje.mymap.MainActivity.Companion.TAG

class CafeMapActivity : AppCompatActivity(),OnMapReadyCallback {
    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        lateinit var naverMap: NaverMap
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationOverlay : LocationOverlay
    private lateinit var callAPI: CallAPI
    var count_ediya :Boolean = false
    var count_star :Boolean = false
    var count_two :Boolean = false
    var count_other :Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"CafeMapActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_map)
        val ediya = findViewById<Button>(R.id.ediya)
        ediya.setOnClickListener{
            Log.d(TAG, "이디야 - called ")
            if(!count_ediya){
                callAPI.del_all()
                callAPI.loadCafe("이디야")//showcafe가 호출되는 시점에서 api가 아직 결과를 못 가져 왔음
                count_ediya = true
            }
            else{
                callAPI.del(Color.BLUE)
                count_ediya = false
            }
        }
        val two = findViewById<Button>(R.id.twosome)
        two.setOnClickListener{
            Log.d(TAG,"투썸 () called")
            if(!count_two){
                callAPI.del_all()
                (callAPI.loadCafe("투썸"))
                count_two = true
            }
            else{
                callAPI.del()
                count_two = false
            }
        }
        val star = findViewById<Button>(R.id.starbucks)
        star.setOnClickListener{
            Log.d(TAG,"스벜 () called")
            if(!count_star){
                callAPI.del_all()
                callAPI.loadCafe("스타벅스")
                count_star = true
            }
            else{
                callAPI.del(Color.GREEN)
                count_star = false
            }
        }
        val other = findViewById<Button>(R.id.other)
        other.setOnClickListener{
            Log.d(TAG,"그외 () called")
            if(!count_other){
                callAPI.del_all()
                callAPI.loadCafe_other()
                count_other = true
                count_ediya = false
                count_star = false
                count_two = false
            }
            else{
                callAPI.del(Color.GRAY)
                count_other = false
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
        Log.d(TAG,"CafeMapActivity - onCreate() end")

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
        callAPI = CallAPI(locationOverlay,this)

    }
}