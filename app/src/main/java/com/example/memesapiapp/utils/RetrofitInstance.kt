package com.example.memesapiapp.utils

import com.example.memesapiapp.data.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    //By lazy = late-init vars
    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Util.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}