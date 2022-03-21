package com.example.climapp.ui.model

data class CurrentWeatherFormat(
    val temp : String = "",
    val symbol: String = "",
    var status : String = "",
    val weatherDescription : String = "",
    val dt : Long = 0,
    val updateAt : String = "",
    val icon  : String = "",
    val iconUrl : Int = 0,
    val iconSecond : String = "",
    val iconUrlSecond : Int = 0,
    val tempInDayTom : String = "",
    val tempInNightTom : String = "",
    var statusTom : String = "",
    val forecastTom : String = ""
)
