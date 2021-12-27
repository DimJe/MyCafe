package com.Dimje.mymap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchOtherService{
    @GET("v2/local/search/category.json")
    fun getSearchOther(
            @Header("Authorization") REST_API_KEY: String,
            @Query("x") x: Double,
            @Query("y") y: Double,
            @Query("category_group_code") category_group_code : String,
            @Query("radius") radius : Int




    ): Call<Cafeinfo>


}