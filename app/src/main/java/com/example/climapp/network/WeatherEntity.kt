package com.example.climapp.network

import com.example.climapp.ui.model.Current

data class WeatherEntity(
    val base: String,
    val main: Main,
    val sys: Sys,
    val id: Int,
    val name: String
)
