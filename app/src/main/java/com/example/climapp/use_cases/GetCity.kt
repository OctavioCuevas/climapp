package com.example.climapp.use_cases

import com.example.climapp.extra.Retrofit
import com.example.climapp.network.services.CityService
import com.example.climapp.ui.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class GetCity {
    private val retrofit = Retrofit.getRetrofit().create(CityService::class.java)

    suspend fun getCityService(
        lat: String,
        lon: String,
        appid: String
    ): Response<List<City>> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.getCitiesByLatLng(lat, lon, appid)
            response
        }
    }
}