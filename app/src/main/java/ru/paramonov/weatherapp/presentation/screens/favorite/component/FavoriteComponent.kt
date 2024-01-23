package ru.paramonov.weatherapp.presentation.screens.favorite.component

import kotlinx.coroutines.flow.StateFlow
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.screens.favorite.store.FavoriteStore

interface FavoriteComponent {

    val model: StateFlow<FavoriteStore.State>

    fun onClickToSearch()

    fun onClickAddToFavorite()

    fun onClickCityItem(city: City)
}
