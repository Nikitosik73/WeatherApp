package ru.paramonov.weatherapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.paramonov.weatherapp.data.network.model.CityDto
import ru.paramonov.weatherapp.data.network.model.CurrentWeatherResponseDto
import ru.paramonov.weatherapp.data.network.model.ForecastWeatherResponseDto

interface ApiService {

    @GET("current.json?")
    suspend fun loadCurrentWeather(
        @Query(QUERY) query: String
    ): CurrentWeatherResponseDto

    @GET("forecast.json?")
    suspend fun loadForecastWeather(
        @Query(QUERY) query: String,
        @Query(DAYS) daysCount: Int = COUNT_DAY
    ): ForecastWeatherResponseDto

    @GET("search.json?")
    suspend fun searchCites(
        @Query(QUERY) query: String
    ): List<CityDto>

    companion object {

        private const val QUERY = "q"
        private const val DAYS = "days"

        private const val COUNT_DAY = 7
    }
}