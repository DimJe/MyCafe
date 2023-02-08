package com.Dimje.mymap.Repository

import com.Dimje.mymap.API.ApiService
import com.Dimje.mymap.API.ApiState
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class RemoteRepository {

    private val api = ApiService.instance

    suspend fun searchCafe(name : String,x:Double,y:Double) : Flow<ApiState<Cafeinfo>> = flow {
        try {
            val response = api.getSearchCafe(R.string.key.toString(),x,y,"CE7",2000,name)
            if(response.isSuccessful){
                response.body()?.let{
                    emit(ApiState.Success(it))
                }
            }
            else{
                try {
                    emit(ApiState.Error(response.errorBody()!!.toString()))
                }catch (e:IOException){
                    e.stackTrace
                }
            }
        }catch (e : Exception){
            emit(ApiState.Error(e.message ?:""))
        } as Unit
    }.flowOn(Dispatchers.IO)

    suspend fun searchAllCafe(x:Double,y:Double) : Flow<ApiState<Cafeinfo>> = flow {
        try {
            val response = api.getSearchOther(R.string.key.toString(),x,y,"CE7",1000)
            if(response.isSuccessful){
                response.body()?.let{
                    emit(ApiState.Success(it))
                }
            }
            else{
                try {
                    emit(ApiState.Error(response.errorBody()!!.toString()))
                }catch (e:IOException){
                    e.stackTrace
                }
            }
        }catch (e : Exception){
            emit(ApiState.Error(e.message ?:""))
        } as Unit
    }.flowOn(Dispatchers.IO)
}