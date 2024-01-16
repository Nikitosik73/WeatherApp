package ru.paramonov.weatherapp.domain.usecase

import ru.paramonov.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class ObserveFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    operator fun invoke(cityId: Int) = repository.observerIsFavorite(cityId = cityId)
}