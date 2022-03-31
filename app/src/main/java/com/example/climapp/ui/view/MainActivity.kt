package com.example.climapp.ui.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.climapp.R
import com.example.climapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.preference.PreferenceManager
import com.example.climapp.exceptions.*
import com.example.climapp.extra.checkPermissions
import com.example.climapp.ui.viewmodels.MainActivityVM
import com.example.climapp.use_cases.CurrentWeather
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    //private val TAG = "WeatherError"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 17//random number

    //Settings
    private var units = false
    private var language = false

    private val viewModel: MainActivityVM by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
        //setupActionBar()
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initComponents() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        units = sharedPreferences.getBoolean("units", false)
        language = sharedPreferences.getBoolean("language", false)

        //binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        viewModel.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setWeatherData()
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun showSnackbar(
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            getString(snackStrId),
            LENGTH_INDEFINITE
        )
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }

    private fun setWeatherData() {
        try {
            val curWeather = CurrentWeather()
            curWeather.viewModel = viewModel
            curWeather.apiID = getString(R.string.api_key)
            curWeather.units = units
            curWeather.lang = language
            curWeather.owner = this
            curWeather.lifecycleScope = lifecycleScope
            curWeather.resources = resources
            curWeather.packageName = this.packageName
            curWeather.binding = binding
            curWeather.context = this
            if (!checkPermissions(this)) {
                curWeather.requestPermissions(this)
            } else {
                curWeather.getLastLocation(fusedLocationClient)
            }
        } catch (ex: Exception) {
            //Log.e(TAG, "Error: " + ex.message)
            when (ex) {
                is CurrentCityNotFoundException ->
                    showSnackbar(R.string.city_not_found)
                is CurrentWeatherNotFoundException ->
                    showSnackbar(R.string.weather_not_found)
                is NeverAskPermissionException ->
                    startLocationPermissionRequest()
                is NoLocationFoundException ->
                    showSnackbar(R.string.location_not_found)
                is PermissionDeniedException -> {
                    showSnackbar(R.string.permission_retionale, android.R.string.ok) {
                        //Request permission
                        startLocationPermissionRequest()
                    }
                }
                else ->
                    showSnackbar(R.string.error_ocurred)
            }
        }
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        units = sharedPreferences.getBoolean("units", false)
        language = sharedPreferences.getBoolean("language", false)
    }
}