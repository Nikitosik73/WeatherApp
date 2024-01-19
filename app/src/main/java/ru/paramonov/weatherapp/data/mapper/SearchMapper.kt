package ru.paramonov.weatherapp.data.mapper

import ru.paramonov.weatherapp.data.network.model.CityDto
import ru.paramonov.weatherapp.domain.entity.City

fun CityDto.toEntity() = City(id, name, country)

fun List<CityDto>.toEntities() = map { it.toEntity() }