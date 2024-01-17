package ru.paramonov.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherDayDto(
    @SerializedName("avgtemp_c") val temperatureC: Float,
    @SerializedName("condition") val conditionWeather: ConditionDto
)
