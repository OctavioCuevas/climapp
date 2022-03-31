package com.example.climapp.use_cases

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climapp.databinding.ActivityMainBinding
import com.example.climapp.exceptions.NeverAskPermissionException
import com.example.climapp.exceptions.NoLocationFoundException
import com.example.climapp.exceptions.NullApiKeyException
import com.example.climapp.exceptions.PermissionDeniedException
import com.example.climapp.ui.adapters.ByHoursAdapter
import com.example.climapp.ui.model.City
import com.example.climapp.ui.model.Current
import com.example.climapp.ui.model.CurrentWeatherFormat
import com.example.climapp.ui.model.OneCall
import com.example.climapp.ui.viewmodels.MainActivityVM
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import coil.load
import com.example.climapp.R

class CurrentWeather {
    private var latitude = ""
    private var longitude = ""
    private var unit = ""
    private var languageCode = ""
    var apiID: String = ""
        set(value) {
            field = value.ifEmpty { throw NullApiKeyException() }
        }
    var units = false
        set(value) {
            this.unit = if (value) "imperial" else "metric"
            field = value
        }
    var lang = false
        set(value) {
            languageCode = if (value) "en" else "sp"
            field = value
        }
    lateinit var viewModel: MainActivityVM
    lateinit var owner: LifecycleOwner
    lateinit var lifecycleScope: LifecycleCoroutineScope
    lateinit var resources: Resources
    lateinit var binding: ActivityMainBinding
    lateinit var context: Context
    var packageName = ""

    @SuppressLint("MissingPermission")
    fun getLastLocation(fusedLocation: FusedLocationProviderClient) {
        fusedLocation.lastLocation.addOnCompleteListener { taskLocation ->
            if (taskLocation.isSuccessful && taskLocation.result != null) {
                val location = taskLocation.result
                this.latitude = location?.latitude.toString()
                this.longitude = location?.longitude.toString()
                if (this.longitude != "" && this.latitude != "") {
                    this.getWeatherData()
                    this.getCityData()
                    this.observers()
                }
            } else {
                throw NoLocationFoundException()
            }
        }
    }

    fun requestPermissions(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            throw PermissionDeniedException()
        } else {
            throw NeverAskPermissionException()
        }

    }

    private fun getWeatherData() {
        this.viewModel.getCurrentWeather(
            this.latitude,
            this.longitude,
            this.unit,
            this.languageCode,
            this.apiID
        )
    }

    private fun getCityData() {
        this.viewModel.getCurrentCity(this.latitude, this.longitude, this.apiID)
    }

    private fun observers() {
        viewModel.getWeatherResponse.observe(owner) { weatherEntity: OneCall ->
            lifecycleScope.launch {
                weatherEntity.apply {
                    val weatherFormat =
                        formatCurrentWeather(weatherEntity, units, resources, packageName)
                    showCurrentWeatherData(weatherFormat, weatherEntity)
                }
            }
        }

        viewModel.getCityResponse.observe(owner) { cityEntity: List<City> ->
            lifecycleScope.launch {
                cityEntity.apply {
                    formatCurrentCity(cityEntity)
                }
            }
        }
    }

    private fun formatCurrentWeather(
        weatherEntity: OneCall,
        units: Boolean,
        resources: Resources,
        packageName: String
    ): CurrentWeatherFormat {
        try {
            val symbol = if (units) "°F" else "°C"
            val weatherDescription = weatherEntity.current.weather[0].description
            val dt = weatherEntity.current.dt
            val icon = weatherEntity.current.weather[0].icon.replace('n', 'd')
            val iconSecond = weatherEntity.daily[1].weather.first().icon.replace('n', 'd')
            val forecastTom = weatherEntity.daily[1].weather.first().description
            //showIndicator(false)
            return CurrentWeatherFormat(
                "${weatherEntity.current.temp.toInt()}",
                symbol,
                if (weatherDescription.isNotEmpty()) (weatherDescription[0].uppercaseChar() + weatherDescription.substring(
                    1
                )) else "Unknown",
                weatherDescription,
                dt,
                SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(Date(dt * 1000)),
                icon,
                resources.getIdentifier("ic_weather_$icon", "drawable", packageName),
                iconSecond,
                resources.getIdentifier("ic_weather_$iconSecond", "drawable", packageName),
                weatherEntity.daily[1].temp.day.toInt().toString(),
                "/" + weatherEntity.daily[1].temp.night.toInt().toString() + symbol,
                if (forecastTom.isNotEmpty()) (forecastTom[0].uppercaseChar() + forecastTom.substring(
                    1
                )) else "NaN"
            )
        } catch (exception: Exception) {
            //showIndicator(false)
            return CurrentWeatherFormat()
        }

    }

    private fun showCurrentWeatherData(
        prettyWeather: CurrentWeatherFormat?,
        weatherEntity: OneCall
    ) {
        binding.apply {
            tvDate.text = prettyWeather?.updateAt
            tvDegrees.text = prettyWeather?.temp
            tvSymbol.text = prettyWeather?.symbol
            tvStatus.text = prettyWeather?.status
            ivStatus.load(prettyWeather!!.iconUrl)

            //Show Components
            tvCity.visibility = View.VISIBLE
            tvSymbol.visibility = View.VISIBLE
            tvDegrees.visibility = View.VISIBLE
            tvDate.visibility = View.VISIBLE
            ivStatus.visibility = View.VISIBLE
            tvStatus.visibility = View.VISIBLE
            tvTempMin.visibility = View.VISIBLE
            tvTempMax.visibility = View.VISIBLE
            tvFeelsLike.visibility = View.VISIBLE
            llHumidity.visibility = View.VISIBLE
            llInfo.visibility = View.VISIBLE
            llPressure.visibility = View.VISIBLE
            llSunrise.visibility = View.VISIBLE
            llSunset.visibility = View.VISIBLE
            llWind.visibility = View.VISIBLE
            progressBarIndicator.visibility = View.INVISIBLE
            setRecyclerByHour(weatherEntity.hourly, recyclerViewHours)
        }
    }

    private fun formatCurrentCity(cityEntity: List<City>) {
        val cityName = cityEntity[0].name
        val country = cityEntity[0].country
        val address = "$cityName, $country"

        binding.apply {
            tvCity.text = address
        }
    }

    private fun setRecyclerByHour(hours: List<Current>, recyclerView: RecyclerView) {
        val byHourAdapter = ByHoursAdapter(context, hours)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = byHourAdapter
        }
    }
}