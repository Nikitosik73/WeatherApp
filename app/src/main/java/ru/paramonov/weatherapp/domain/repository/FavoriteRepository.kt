package ru.paramonov.weatherapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.paramonov.weatherapp.domain.entity.City

interface FavoriteRepository {

    val favoriteCities: Flow<City>

    fun observerIsFavorite(cityId: Int): Flow<Boolean>

    suspend fun addToFavorite(city: City)

    suspend fun removeFromFavorite(cityId: Int)
}
