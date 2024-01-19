package ru.paramonov.weatherapp.data.repository

import ru.paramonov.weatherapp.data.mapper.toEntities
import ru.paramonov.weatherapp.data.mapper.toEntity
import ru.paramonov.weatherapp.data.network.api.ApiService
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {

    override suspend fun search(query: String): List<City> {
        return apiService.searchCites(query = query).toEntities()
    }
}