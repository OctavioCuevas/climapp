package com.example.climapp.network.services

import com.example.climapp.network.WeatherEntity
import com.example.climapp.ui.model.OneCall
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Weather {
    @GET("data/2.5/onecall")
    suspend fun getWeatherByLoc(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String?,
        @Query("lang") lang: String?,
        @Query("appid") appid: String
    ): Response<OneCall>
}