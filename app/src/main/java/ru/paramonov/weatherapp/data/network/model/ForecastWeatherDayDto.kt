package ru.paramonov.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ForecastWeatherDayDto(
    @SerializedName("forecastday") val forecastDays: List<DayDto>
)