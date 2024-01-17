package ru.paramonov.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("last_updated_epoch") val date: Long,
    @SerializedName("temp_c") val temperatureC: Float,
    @SerializedName("condition") val conditionWeather: ConditionDto
)