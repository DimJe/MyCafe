package com.Dimje.mymap.Repository

import android.util.Log
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class DBRepository(private val db : DatabaseReference) {

    suspend fun getReviewData(key: String) : Flow<ResultState<List<Review>>> = flow {
        try {
            val reviewList = ArrayList<Review>()
            val snap = db.child(key).get().await()
            snap.children.forEach {
                reviewList.add(it.getValue(Review::class.java)!!)
            }
            emit(ResultState.Success(reviewList as List<Review>))
        }catch (e : Exception){
            emit(ResultState.Error(e.message ?:""))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addReview(key: String,review: Review){
        db.child(key).push().setValue(review)
    }
}