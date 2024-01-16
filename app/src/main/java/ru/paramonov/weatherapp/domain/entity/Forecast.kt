package ru.paramonov.weatherapp.domain.entity

data class Forecast(
    val currentWeather: Weather,
    val upcomingWeather: List<Weather>
)
