package ru.paramonov.weatherapp.domain.entity

import java.util.Calendar

data class Weather(
    val temperatureC: Float,
    val conditionText: String,
    val conditionUrl: String,
    val date: Calendar
)
