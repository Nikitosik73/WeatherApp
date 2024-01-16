package ru.paramonov.weatherapp.domain.repository

import ru.paramonov.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}
