package com.Dimje.mymap.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.model
import com.Dimje.mymap.Review
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DBViewModel : ViewModel() {
    val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var reviewList : MutableLiveData<ArrayList<Review>> = MutableLiveData()
    var temp = ArrayList<Review>()
    var taste : Double = 0.0
    var beauty : Double = 0.0
    var study : Double = 0.0
    var count : Int = 0
    fun createReview(
        review: String = "",
        taste: String = "",
        beauty: String = "",
        study: String = "",
        position: Int
    ) {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val review = Review(review, taste, beauty, study, date)
        mDatabase.child(model.result.value!!.documents[position].place_name).push().setValue(review)

    }

    fun getData(position: Int){
        mDatabase.child(model.result.value!!.documents[position].place_name).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                taste = 0.0
                beauty = 0.0
                study = 0.0
                count = 0
                temp.clear()
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
                    temp.add(review)
                }
                reviewList.value = temp
            }
        })
    }
}