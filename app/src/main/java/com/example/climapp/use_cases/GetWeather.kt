package com.example.climapp.use_cases

import com.example.climapp.extra.Retrofit
import com.example.climapp.network.services.Weather
import com.example.climapp.ui.model.OneCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class GetWeather {
    private val retrofit = Retrofit.getRetrofit().create(Weather::class.java)

    suspend fun getWeatherService(
        lat: String,
        lon: String,
        units: String?,
        lang: String?,
        appID: String
    ): Response<OneCall> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.getWeatherByLoc(lat, lon, units, lang, appID)
            response
        }
    }
}