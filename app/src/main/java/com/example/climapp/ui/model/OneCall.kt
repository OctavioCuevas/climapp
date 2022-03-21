package com.example.climapp.ui.model

import java.io.Serializable

data class OneCall(
    val current: Current,
    val hourly: List<Current>,
    val daily: List<Daily>,
    var city: City?
): Serializable
