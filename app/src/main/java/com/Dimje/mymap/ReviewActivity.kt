package com.Dimje.mymap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.API.CallAPI.Companion.result
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.mDatabase
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

        cafeName.text = result.value!!.documents[position].place_name
        cafeAddress.text=result.value!!.documents[position].address_name


        val reAdapter = ReviewAdapter()
        reAdapter.submitList(reviewList)
        reviewRecyclerView.adapter = reAdapter
        reviewRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        mDatabase.child(result.value!!.documents[position].place_name).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                reviewList.clear()
                for (data in snapshot.children){
                    val review = Review(data.child("review").value.toString(),
                                        data.child("taste").value.toString(),
                                        data.child("beauty").value.toString(),
                                        data.child("study").value.toString(),
                                        data.child("date").value.toString())
                    Log.d(TAG, "onDataChange: ${review.review}")
                    reviewList.add(review)
                }
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