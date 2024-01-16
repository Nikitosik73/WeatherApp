package ru.paramonov.weatherapp.domain.usecase

import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun addToFavorite(city: City) = repository.addToFavorite(city = city)

    suspend fun removeFromFavorite(cityId: Int) = repository.removeFromFavorite(cityId = cityId)
}