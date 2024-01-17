package ru.paramonov.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponseDto(
    @SerializedName("current") val currentWeatherContent: WeatherDto,
    @SerializedName("forecast") val forecastWeatherContent: ForecastWeatherDayDto
)