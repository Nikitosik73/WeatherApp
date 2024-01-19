package ru.paramonov.weatherapp.data.repository

import ru.paramonov.weatherapp.data.mapper.toEntity
import ru.paramonov.weatherapp.data.network.api.ApiService
import ru.paramonov.weatherapp.domain.entity.Forecast
import ru.paramonov.weatherapp.domain.entity.Weather
import ru.paramonov.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): WeatherRepository {

    override suspend fun getCurrentWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather(query = "$PREFIX_CITY_ID$cityId").toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecastWeather(query = "$PREFIX_CITY_ID$cityId").toEntity()
    }

    private companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}