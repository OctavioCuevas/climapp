package com.example.climapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climapp.exceptions.CurrentCityNotFoundException
import com.example.climapp.exceptions.CurrentWeatherNotFoundException
import com.example.climapp.ui.model.City
import com.example.climapp.ui.model.OneCall
import com.example.climapp.use_cases.GetCity
import com.example.climapp.use_cases.GetWeather
import kotlinx.coroutines.launch

class MainActivityVM : ViewModel() {
    //Services
    private lateinit var serviceGetWeather: GetWeather
    private lateinit var serviceGetCity: GetCity

    //Live Data
    val getWeatherResponse = MutableLiveData<OneCall>()
    val getCityResponse = MutableLiveData<List<City>>()

    fun onCreate() {
        serviceGetWeather = GetWeather()
        serviceGetCity = GetCity()
    }

    fun getCurrentWeather(lat: String, lon: String, units: String?, lang: String?, appID: String) {
        this.viewModelScope.launch {
            val response = serviceGetWeather.getWeatherService(lat, lon, units, lang, appID)
            if (response.isSuccessful) {
                getWeatherResponse.postValue(response.body())
            } else {
                throw CurrentWeatherNotFoundException()
            }
        }
    }

    fun getCurrentCity(lat: String, lon: String, appID: String) {
        viewModelScope.launch {
            val response = serviceGetCity.getCityService(lat, lon, appID)
            if (response.isSuccessful) {
                getCityResponse.postValue(response.body())
            } else {
                throw CurrentCityNotFoundException()
            }
        }
    }

}