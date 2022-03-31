package com.example.climapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climapp.databinding.ActivityMainBinding
import com.example.climapp.exceptions.CurrentCityNotFoundException
import com.example.climapp.exceptions.CurrentWeatherNotFoundException
import com.example.climapp.ui.model.City
import com.example.climapp.ui.model.OneCall
import com.example.climapp.use_cases.GetCity
import com.example.climapp.use_cases.GetWeather
import kotlinx.coroutines.launch

class MainActivityVM : ViewModel() {

    private var TAG = "WeatherError"
    //Services
    lateinit var serviceGetWeather: GetWeather
    lateinit var serviceGetCity: GetCity

    //LiveDatas
    val getWeatherResponse = MutableLiveData<OneCall>()
    val getCityResponse = MutableLiveData<List<City>>()

    private lateinit var binding: ActivityMainBinding

    fun onCreate() {
        serviceGetWeather = GetWeather()
        serviceGetCity = GetCity()
    }

    //Obtenemos el clima actual
    fun getCurrentWeather(lat: String, lon: String, units: String?, lang: String?, appid: String) {
        this.viewModelScope.launch {
            val response = serviceGetWeather.getWeatherService(lat, lon, units, lang, appid)
            if (response.isSuccessful) {
                Log.e(TAG, "Todo correcto" + response.body())
                getWeatherResponse.postValue(response.body())
            } else {
                throw CurrentWeatherNotFoundException()
            }
        }
    }

    //Obtenemos la ciudad actual
    fun getCurrentCity(lat: String, lon: String, appid: String) {
        viewModelScope.launch {
            val response = serviceGetCity.getCityService(lat, lon, appid)
            if (response.isSuccessful) {
                getCityResponse.postValue(response.body())
            } else {
                throw CurrentCityNotFoundException()
            }
        }
    }

}