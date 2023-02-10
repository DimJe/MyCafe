package com.Dimje.mymap

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.Dimje.mymap.API.ApiState
import com.Dimje.mymap.ViewModel.APIViewModel
import com.Dimje.mymap.ViewModel.DBViewModel
import com.Dimje.mymap.databinding.ActivityMainBinding
import com.Dimje.mymap.databinding.DialogMinigameBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        //lateinit var locationOverlay: LocationOverlay
        const val TAG: String = "로그"
        val dbModel = DBViewModel()
    }
    private lateinit var locationSource: FusedLocationSource
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
        //locationOverlay = naverMap.locationOverlay
        val ui: UiSettings = naverMap.uiSettings
        ui.isLocationButtonEnabled = true

    }
    private fun initView(){
        binding.mapWithBrand.setOnClickListener {

            val intent = Intent(this, CafeBrandCheckBox::class.java)
            startActivity(intent)
        }
        binding.miniGame.setOnClickListener {

            val random = Random().nextInt(11) + 1

            val dialogBinding = DialogMinigameBinding.inflate(layoutInflater)
            dialogBinding.grid.children.forEach {
                val anim = AnimationUtils.loadAnimation(applicationContext,R.anim.slide_down)
                it.setOnClickListener { textView ->

                    textView.startAnimation(anim)
                    if(textView.tag.toString().toInt() == random){
                        (textView as TextView).text = "당첨"
                        dialogBinding.result.text = "기분 좋게 커피를 사주세요"
                    }
                    else{
                        (textView as TextView).text = "꽝"
                    }
                }
            }
            AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .setCancelable(true)
                .show()
        }
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.apply {
                    cafeDatas.collect{
                        when(it){
                            is ApiState.Success -> {
                                it.data?.let { data ->
                                    show(data.documents)
                                }
                            }
                            is ApiState.Error -> {

                            }
                            is ApiState.Loading -> {}
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

    private fun delAll() {
        if (markerList.isEmpty()) return
        markerList.forEach {
            it.map = null
        }
        markerList.clear()
    }
}




