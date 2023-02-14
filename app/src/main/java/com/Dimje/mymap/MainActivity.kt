package com.Dimje.mymap

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.Dimje.mymap.Repository.ResultState
import com.Dimje.mymap.UI.dialog.DialogListener
import com.Dimje.mymap.UI.dialog.MiniGameDialog
import com.Dimje.mymap.ViewModel.APIViewModel
import com.Dimje.mymap.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), OnMapReadyCallback,DialogListener {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val TAG: String = "로그"
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationClient : FusedLocationProviderClient
    private lateinit var naverMap: NaverMap
    private var markerList = mutableListOf<Marker>()
    private val viewModel : APIViewModel by viewModel()

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

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

        naverMap = NaverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.addOnLocationChangeListener { location ->
            naverMap.locationOverlay.position = LatLng(location.latitude, location.longitude)
        }
        val ui: UiSettings = naverMap.uiSettings
        ui.isLocationButtonEnabled = true

    }
    private fun initView(){
        binding.slidingPanel.isTouchEnabled = true
        binding.mapWithBrand.setOnClickListener {
            viewModel.requestCafeData("이디야",naverMap.locationOverlay.position.longitude,naverMap.locationOverlay.position.latitude)
        }
        binding.miniGame.setOnClickListener {
            val miniGame = MiniGameDialog(this,0)
            miniGame.show(this.supportFragmentManager,"MiniGame")
        }

        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.apply {
                    cafeDatas.collect{
                        when(it){
                            is ResultState.Success -> {
                                it.data?.let { data ->
                                    delAll()
                                    show(data.documents)
                                }
                            }
                            is ResultState.Error -> {
                                Log.d(TAG, "Error: ${it.message} ")
                            }
                            is ResultState.Loading -> {}
                        }
                    }
                }
            }
        }

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
                openSlidingLayout()
                false
            }
            marker.position = LatLng(cafeInfo.y.toDouble(), cafeInfo.x.toDouble())
            marker.map = naverMap
            markerList.add(marker)
            infoWindow.open(marker)
        }
    }

    private fun delAll() {
        if (markerList.isEmpty()) return
        markerList.forEach {
            it.map = null
        }
        markerList.clear()
    }
    private fun openSlidingLayout(){
        if (binding.slidingPanel.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            binding.slidingPanel.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
        }
    }

    override fun onSubmitClick(id: Int,review: String,point: Float) {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    override fun onSearchClick(id: Int,type: String) {
    }
}




