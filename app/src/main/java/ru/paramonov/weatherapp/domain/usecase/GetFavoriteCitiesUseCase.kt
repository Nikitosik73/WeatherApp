package ru.paramonov.weatherapp.domain.usecase

import ru.paramonov.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    operator fun invoke() = repository.favoriteCities
}