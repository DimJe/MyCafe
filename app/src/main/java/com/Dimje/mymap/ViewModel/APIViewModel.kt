package com.Dimje.mymap.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Dimje.mymap.Repository.ResultState
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.Repository.DBRepository
import com.Dimje.mymap.Repository.RemoteRepository
import com.Dimje.mymap.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class APIViewModel(val remoteRepository: RemoteRepository,val dbRepository: DBRepository) : ViewModel(){

    var mCafeDatas : MutableStateFlow<ResultState<Cafeinfo>> = MutableStateFlow(ResultState.Loading())
    var cafeDatas : StateFlow<ResultState<Cafeinfo>> = mCafeDatas

    var mReviewData : MutableStateFlow<ResultState<List<Review>>> = MutableStateFlow(ResultState.Loading())
    var reviewData : StateFlow<ResultState<List<Review>>> = mReviewData

    fun requestCafeData(name : String,x:Double,y:Double) = viewModelScope.launch {
        mCafeDatas.value = ResultState.Loading()
        Log.d(TAG, "requestCafeData: called")
        remoteRepository.searchCafe(name,x,y)
            .catch { error ->
                mCafeDatas.value = ResultState.Error("${error.message}")
            }
            .collect{ value ->
                mCafeDatas.value = value
            }
    }

    fun requestReviewData(key: String) = viewModelScope.launch {
        Log.d(TAG, "requestReviewData: called")
        mReviewData.value = ResultState.Loading()
        dbRepository.getReviewData(key)
            .catch { error -> mReviewData.value = ResultState.Error("${error.message}") }
            .collect{ value -> mReviewData.value = value}
    }
    fun addReview(key: String,review: Review){
        viewModelScope.launch {
            dbRepository.addReview(key, review)
        }
    }


    


}