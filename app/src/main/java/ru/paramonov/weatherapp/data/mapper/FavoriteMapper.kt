package ru.paramonov.weatherapp.data.mapper

import ru.paramonov.weatherapp.data.local.model.CityDbModel
import ru.paramonov.weatherapp.domain.entity.City

fun City.toDbModel() = CityDbModel(id, name, country)

fun CityDbModel.toEntity() = City(id, name, country)

fun List<CityDbModel>.toEntities() = map { dbModel -> dbModel.toEntity() }