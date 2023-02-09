package com.Dimje.mymap.DI

import com.Dimje.mymap.Repository.RemoteRepository
import com.Dimje.mymap.SearchCafeService
import com.Dimje.mymap.ViewModel.APIViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module{

    single<SearchCafeService>(){
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    single<RemoteRepository>{ RemoteRepository(get()) }
    viewModel { APIViewModel(get()) }

}