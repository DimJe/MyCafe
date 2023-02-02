package com.Dimje.mymap.API

import com.Dimje.mymap.SearchCafeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL_KAKAO_API = "https://dapi.kakao.com/"
    private const val REST_API_KEY = "KakaoAK b6687296c27e98184bd039bd2e288f48"

    val instance : SearchCafeService = Retrofit.Builder()
        .baseUrl(BASE_URL_KAKAO_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchCafeService::class.java)
}