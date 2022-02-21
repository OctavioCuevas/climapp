package com.example.climapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeatherById(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("id") id: Long,
        @Query("units") units: String?,
        @Query("appid") appid: String
    ) : WeatherEntity
}