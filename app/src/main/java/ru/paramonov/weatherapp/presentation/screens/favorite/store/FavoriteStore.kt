package ru.paramonov.weatherapp.presentation.screens.favorite.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.paramonov.weatherapp.domain.entity.City

interface FavoriteStore : Store<FavoriteStore.Intent, FavoriteStore.State, FavoriteStore.Label> {

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {

            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(val temperatureC: Float, val conditionImageUrl: String) : WeatherState
        }
    }

    sealed interface Intent {

        data object ClickToSearch : Intent

        data object ClickAddToFavorite : Intent

        data class CityItemClicked(val city: City) : Intent
    }

    sealed interface Label {

        data object ClickToSearch : Label

        data object ClickAddToFavorite : Label

        data class CityItemClicked(val city: City) : Label
    }
}
