package com.Dimje.mymap.RecyclerView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.*
import com.Dimje.mymap.MainActivity.Companion.model
import com.Dimje.mymap.databinding.ActivityCafeListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CafeListActivity : AppCompatActivity(),RecyclerViewInterface {


    private lateinit var recyclerAdapter: RecyclerViewAdapter
    private val binding : ActivityCafeListBinding by lazy {
        ActivityCafeListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d(MainActivity.TAG, "onCreate: ListActivity")

        recyclerAdapter = RecyclerViewAdapter(this)


        // 리사이클러뷰 설정
        binding.listWithBrand.setOnClickListener {
            val intent = Intent(this, CafeBrandCheckBox::class.java)
            startActivity(intent)
        }
        binding.lookAsMap.setOnClickListener {
            finish()
        }
        model.result.observe(this, Observer {
            recyclerAdapter.submitList(model.result.value!!.documents as ArrayList<Document>)
            binding.recyclerView.apply {

                // 리사이클러뷰 방향 등 설정
                layoutManager = LinearLayoutManager(this@CafeListActivity, LinearLayoutManager.VERTICAL, false)

                // 어답터 장착
                adapter = recyclerAdapter
            }
        })

    }

    override fun onItemClicked(position: Int){
        Log.d(MainActivity.TAG, "onItemClicked: called")
        val intent = Intent(this,ReviewActivity::class.java)
        intent.putExtra("position",position)
        startActivity(intent)
    }
}