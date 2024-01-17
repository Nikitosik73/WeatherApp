package ru.paramonov.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponseDto(
    @SerializedName("current") val currentWeatherContent: WeatherDto
)