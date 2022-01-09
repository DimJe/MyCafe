package com.Dimje.mymap.RecyclerView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.Document
import com.Dimje.mymap.R
import kotlinx.android.synthetic.main.activity_cafe_list.*

class CafeListActivity : AppCompatActivity(),RecyclerViewInterface {

    var modelList = ArrayList<Document>()

    private lateinit var RecyclerAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_list)

        RecyclerAdapter = RecyclerViewAdapter(this)

        RecyclerAdapter.submitList(this.modelList)

        // 리사이클러뷰 설정
        recycler_view.apply {

            // 리사이클러뷰 방향 등 설정
            layoutManager = LinearLayoutManager(this@CafeListActivity, LinearLayoutManager.VERTICAL, false)

            // 어답터 장착
            adapter = RecyclerAdapter
        }
    }

    override fun onItemClicked(position: Int){

    }
}