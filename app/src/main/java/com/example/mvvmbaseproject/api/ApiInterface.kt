package com.example.mvvmbaseproject.api

import com.example.mvvmbaseproject.model.MovieData
import com.example.mvvmbaseproject.model.MovieDataPro
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {
    @GET("/")
    fun movieDataFromServer(@QueryMap  params:Map<String, String>): Single<MovieDataPro>

    @GET("")
    fun getListOfSendSms(
        @Path("apikey") apiKey: String
    ):Single<MovieData>


}