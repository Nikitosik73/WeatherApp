package ru.paramonov.weatherapp.domain.usecase

import ru.paramonov.weatherapp.domain.repository.SearchRepository
import ru.paramonov.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int) = repository.getCurrentWeather(cityId = cityId)
}