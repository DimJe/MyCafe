package com.Dimje.mymap

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.Dimje.mymap.RecyclerView.CafeListActivity
import com.Dimje.mymap.ViewModel.APIViewModel
import com.Dimje.mymap.ViewModel.DBViewModel
import com.Dimje.mymap.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

/*  1.api 모든 결과 불러오기
    2.버튼 꾸미기 o
    3.text classification 구현해보기
    4.splash 구현 o
    5.소스파일 나눠보기 o
    6.firebase 연동 o
    7.코드 암호화
    8.평가등록 구현 o
    9.평가 불러오기 구현 o
    10.내 위치 미리 얻어와서 지도가 바로 내 위로 뜰 수 있게 하기
*/
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        val model = APIViewModel()
        lateinit var locationOverlay: LocationOverlay
        const val TAG: String = "로그"
        val dbModel = DBViewModel()
        val mDatabase = FirebaseDatabase.getInstance().reference
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var markerList = mutableListOf<Marker>()
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mapWithBrand.setOnClickListener {

            val intent = Intent(this, CafeBrandCheckBox::class.java)
            startActivity(intent)
        }

        binding.lookAsList.setOnClickListener {
            val intent = Intent(this, CafeListActivity::class.java)
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
        Log.d(TAG, "MainActivity - onCreate() end")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(NaverMap: NaverMap) {

        Log.d(TAG, "MainActivity - onMapReady() called")

        naverMap = NaverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.addOnLocationChangeListener { location ->
            locationOverlay.position = LatLng(location.latitude, location.longitude)
        }
        locationOverlay = naverMap.locationOverlay
        var ui: UiSettings = naverMap.uiSettings
        ui.isLocationButtonEnabled = true

        // 검색 api 결과로 지도에 마커 뿌리기
        model.result.observe(this, Observer {
            Log.d(TAG, "onCreate - observe")
            del_all()
            show(model.result.value!!.documents)
        })

    }

    private fun show(result: List<Document>) {
        for (cafeInfo in result) {
            val marker = Marker()
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.BLUE

            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return cafeInfo.place_name
                }
            }
            marker.setOnClickListener {
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("position", result.indexOf(cafeInfo))
                startActivity(intent)
                false
            }
            marker.position = LatLng(cafeInfo.y.toDouble(), cafeInfo.x.toDouble())
            marker.map = naverMap
            markerList.add(marker)
            infoWindow.open(marker)
        }
    }

    fun del_all() {
        Log.d(TAG, "CallAPI - del_all() called   ${markerList.size}")
        if (markerList.isEmpty()) return
        markerList.forEach {
            it.map = null
        }
        markerList.clear()
        Log.d(TAG, "after_array_size : ${markerList.size}")
    }
}




