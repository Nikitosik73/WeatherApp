package ru.paramonov.weatherapp.data.mapper

import ru.paramonov.weatherapp.data.network.model.CurrentWeatherResponseDto
import ru.paramonov.weatherapp.data.network.model.ForecastWeatherResponseDto
import ru.paramonov.weatherapp.data.network.model.WeatherDto
import ru.paramonov.weatherapp.domain.entity.Forecast
import ru.paramonov.weatherapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun CurrentWeatherResponseDto.toEntity() = currentWeatherContent.toEntity()

fun ForecastWeatherResponseDto.toEntity() = Forecast(
    currentWeather = currentWeatherContent.toEntity(),
    upcomingWeather = forecastWeatherContent.forecastDays.map { dayDto ->
        val weatherDay = dayDto.weatherDay
        Weather(
            temperatureC = weatherDay.temperatureC,
            conditionText = weatherDay.conditionWeather.text,
            conditionUrl = weatherDay.conditionWeather.iconUrl.toCorrectImageUrl(),
            date = dayDto.date.toCalendar()
        )
    }
)

fun WeatherDto.toEntity() = Weather(
    temperatureC = temperatureC,
    conditionText = conditionWeather.text,
    conditionUrl = conditionWeather.iconUrl.toCorrectImageUrl(),
    date = date.toCalendar()
)

private fun String.toCorrectImageUrl() = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128x128"
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}