package com.Dimje.mymap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.mDatabase
import com.Dimje.mymap.MainActivity.Companion.model
import com.Dimje.mymap.RecyclerView.ReviewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_review.*

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
        reAdapter.submitList(reviewList)
        reviewRecyclerView.adapter = reAdapter
        reviewRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        mDatabase.child(model.result.value!!.documents[position].place_name).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var taste : Double = 0.0
                var beauty : Double = 0.0
                var study : Double = 0.0
                var count : Int = 0
                reviewList.clear()
                for (data in snapshot.children){
                    val review = Review(data.child("review").value.toString(),
                                        data.child("taste").value.toString(),
                                        data.child("beauty").value.toString(),
                                        data.child("study").value.toString(),
                                        data.child("date").value.toString())
                    Log.d(TAG, "onDataChange: ${review.review}")
                    taste += review.taste.toDouble()
                    beauty += review.beauty.toDouble()
                    study += review.study.toDouble()
                    count++
                    reviewList.add(review)
                }
                cafePoint.text = if (count==0) "맛있나요? : 0  이쁜가요? : 0  공부하기 좋은가요? : 0"
                                 else "맛있나요? : ${taste/count}  이쁜가요? : ${beauty/count}  공부하기 좋은가요? : ${study/count}"
                reAdapter.notifyDataSetChanged()
            }
        })

        writeReview.setOnClickListener {
            val intent = Intent(this,AddReview::class.java)
            intent.putExtra("position",position)
            startActivity(intent)
        }
        close.setOnClickListener {
            finish()
        }
    }
}