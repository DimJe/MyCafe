package com.Dimje.mymap.RecyclerView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.*
import com.Dimje.mymap.API.CallAPI
import com.Dimje.mymap.API.CallAPI.Companion.result
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_cafe_list.*

class CafeListActivity : AppCompatActivity(),RecyclerViewInterface {


    private lateinit var RecyclerAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_list)

        Log.d(MainActivity.TAG, "onCreate: ListActivity")
        var locationSource =
            FusedLocationSource(this, CafeMapActivity.LOCATION_PERMISSION_REQUEST_CODE)

        RecyclerAdapter = RecyclerViewAdapter(this)


        // 리사이클러뷰 설정
        list_with_brand.setOnClickListener {
            val intent = Intent(this, CafeBrandCheckBox::class.java)
            startActivity(intent)
        }
        result.observe(this, Observer {
//            RecyclerAdapter.submitList(result.value!!.documents as ArrayList<Document>)
//            recycler_view.apply {
//
//                // 리사이클러뷰 방향 등 설정
//                layoutManager = LinearLayoutManager(this@CafeListActivity, LinearLayoutManager.VERTICAL, false)
//
//                // 어답터 장착
//                adapter = RecyclerAdapter
//            }
        })

    }

    override fun onItemClicked(position: Int){

    }
}