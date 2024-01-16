package ru.paramonov.weatherapp.domain.repository

import ru.paramonov.weatherapp.domain.entity.Forecast
import ru.paramonov.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getCurrentWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast
}
