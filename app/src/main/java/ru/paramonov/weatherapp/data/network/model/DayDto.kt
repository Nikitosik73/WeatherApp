package ru.paramonov.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("date_epoch") val date: Long,
    @SerializedName("day") val weatherDay: WeatherDayDto
)