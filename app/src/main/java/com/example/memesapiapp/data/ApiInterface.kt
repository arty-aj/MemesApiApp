package com.example.memesapiapp.data

import com.example.memesapiapp.models.AllMemesData
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("get_memes")
    //suspend fun suspends a coroutine.
    //can only be called by coroutine. AKA the launchEffect in mainActivity
    //Gets a response from the url and saves it to our model, AllMemesData
    suspend fun getMemesList() : Response<AllMemesData>
}