package com.Dimje.mymap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.dbModel
import com.Dimje.mymap.MainActivity.Companion.mDatabase
import com.Dimje.mymap.MainActivity.Companion.model
import com.Dimje.mymap.RecyclerView.ReviewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_review.*
import java.lang.Math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ReviewActivity : AppCompatActivity() {
    var position : Int = 0
    var reviewList = ArrayList<Review>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        Log.d(TAG, "ReviewActivity - onCreate : called ")

        position = intent.getIntExtra("position",-1)

        cafeName.text = model.result.value!!.documents[position].place_name
        cafeAddress.text = model.result.value!!.documents[position].address_name


        val reAdapter = ReviewAdapter()


        dbModel.getData(position)
        dbModel.reviewList.observe(this,{
            reAdapter.submitList(it)
            reviewRecyclerView.adapter = reAdapter
            reviewRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            cafePoint.text = if (dbModel.count==0) "맛있나요? : 0  이쁜가요? : 0  공부하기 좋은가요? : 0"
                             else "맛있나요? : ${(dbModel.taste / dbModel.count).roundToLong()}  " +
                                  "이쁜가요? : ${(dbModel.beauty/ dbModel.count).roundToLong()}  " +
                                  "공부하기 좋은가요? : ${(dbModel.study/ dbModel.count).roundToLong()}"
        })


        writeReview.setOnClickListener {
            val intent = Intent(this,AddReview::class.java)
            intent.putExtra("position",position)
            startActivity(intent)
        }
        close.setOnClickListener {
            dbModel.reviewList.value!!.clear()
            finish()
        }
    }
}