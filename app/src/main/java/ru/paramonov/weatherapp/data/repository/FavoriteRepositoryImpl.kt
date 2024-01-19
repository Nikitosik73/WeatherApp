package ru.paramonov.weatherapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.paramonov.weatherapp.data.local.db.dao.FavoriteCitiesDao
import ru.paramonov.weatherapp.data.mapper.toDbModel
import ru.paramonov.weatherapp.data.mapper.toEntities
import ru.paramonov.weatherapp.data.mapper.toEntity
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteCitiesDao: FavoriteCitiesDao
) : FavoriteRepository {

    override val favoriteCities: Flow<List<City>> = favoriteCitiesDao.getAllFavoriteCities()
        .map { dbModels -> dbModels.toEntities() }

    override fun observerIsFavorite(cityId: Int): Flow<Boolean> {
        return favoriteCitiesDao.observeIsFavorite(cityId = cityId)
    }

    override suspend fun addToFavorite(city: City) {
        favoriteCitiesDao.insertToFavorite(city = city.toDbModel())
    }

    override suspend fun removeFromFavorite(cityId: Int) {
        favoriteCitiesDao.removeFromFavorite(cityId = cityId)
    }
}