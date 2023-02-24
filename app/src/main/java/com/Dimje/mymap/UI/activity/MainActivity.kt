package com.Dimje.mymap.UI.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.Adapter.ReviewAdapter
import com.Dimje.mymap.Document
import com.Dimje.mymap.R
import com.Dimje.mymap.Repository.ResultState
import com.Dimje.mymap.Review
import com.Dimje.mymap.UI.dialog.AddReviewDialog
import com.Dimje.mymap.UI.dialog.DialogListener
import com.Dimje.mymap.UI.dialog.MiniGameDialog
import com.Dimje.mymap.UI.dialog.SearchCafeDialog
import com.Dimje.mymap.ViewModel.ViewModel
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
    private lateinit var adapter: ReviewAdapter
    private var markerList = mutableListOf<Marker>()
    private val viewModel : ViewModel by viewModel()

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
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

        //리사이클러 뷰 설정
        adapter = ReviewAdapter(listOf<Review>()).apply {
            binding.reviewRecyclerView.adapter = this
            binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.reviewRecyclerView.addItemDecoration(DividerItemDecoration(this@MainActivity, VERTICAL))
        }

        binding.slidingPanel.isTouchEnabled = true

        //UI 리스너 설정
        binding.mapWithBrand.setOnClickListener {
            SearchCafeDialog(this,2).apply {
                show(this@MainActivity.supportFragmentManager,"SearchCafe")
            }
        }
        binding.addReview.setOnClickListener {
            AddReviewDialog(this,1).apply {
                show(this@MainActivity.supportFragmentManager,"AddReview")
            }
        }
        binding.miniGame.setOnClickListener {
            MiniGameDialog(this,0).apply {
                show(this@MainActivity.supportFragmentManager,"MiniGame")
            }
        }
        binding.close.setOnClickListener {
            closeSlidingLayout()
        }

        //NAVER MAP 설정
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        //flow collect 설정
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
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
                launch {
                    viewModel.apply {
                        reviewData.collect{
                            when(it){
                                is ResultState.Success -> {
                                    it.data?.let { data ->
                                        binding.cafePoint.text = String.format("%.1f",calPoint(data))
                                        adapter.submit(data)
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
                viewModel.requestReviewData(cafeInfo.place_name)
                openSlidingLayout()
                initSlidingView(cafeInfo)
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
    private fun closeSlidingLayout(){
        if (binding.slidingPanel.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            binding.slidingPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }
    private fun initSlidingView(item: Document){
        binding.cafeName.text = item.place_name
        binding.cafeAddress.text = item.address_name
    }
    private fun calPoint(items: List<Review>) : Double{

        var point = 0.0
        if(items.isEmpty()) return point
        items.forEach {
            point += it.point!!
        }
        return point/items.size
    }

    override fun onBackPressed() {
        if (binding.slidingPanel.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            binding.slidingPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        else super.onBackPressed()
    }

    override fun onSubmitClick(id: Int,review: String,point: Double) {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        viewModel.addReview(binding.cafeName.text.toString(), Review(review, point, date))
        viewModel.requestReviewData(binding.cafeName.text.toString())
    }

    override fun onSearchClick(id: Int,type: String) {
        if(type == "all") viewModel.requestAllCafeData(naverMap.locationOverlay.position.longitude,naverMap.locationOverlay.position.latitude)
        else viewModel.requestCafeData(type,naverMap.locationOverlay.position.longitude,naverMap.locationOverlay.position.latitude)
    }
}



