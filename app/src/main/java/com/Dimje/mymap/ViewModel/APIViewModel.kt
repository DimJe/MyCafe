package com.Dimje.mymap.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Dimje.mymap.API.ApiState
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.Repository.RemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class APIViewModel(val remoteRepository: RemoteRepository) : ViewModel(){

    var mCafeDatas : MutableStateFlow<ApiState<Cafeinfo>> = MutableStateFlow(ApiState.Loading())
    var cafeDatas : StateFlow<ApiState<Cafeinfo>> = mCafeDatas

    fun requestCafeData(name : String,x:Double,y:Double) = viewModelScope.launch {
        mCafeDatas.value = ApiState.Loading()
        remoteRepository.searchCafe(name,x,y)
            .catch { error ->
                mCafeDatas.value = ApiState.Error("${error.message}")
            }
            .collect{ value ->
                mCafeDatas.value = value
            }
    }


    


}