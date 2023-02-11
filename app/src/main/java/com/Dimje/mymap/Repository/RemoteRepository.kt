package com.Dimje.mymap.Repository

import com.Dimje.mymap.API.ApiState
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.SearchCafeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class RemoteRepository(private val retrofit : SearchCafeService) {

    suspend fun searchCafe(name : String,x:Double,y:Double) : Flow<ApiState<Cafeinfo>> = flow {
        try {
            val response = retrofit.getSearchCafe("KakaoAK b6687296c27e98184bd039bd2e288f48",x,y,"CE7",1000,name)
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
            val response = retrofit.getSearchOther("KakaoAK b6687296c27e98184bd039bd2e288f48",x,y,"CE7",1000)
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