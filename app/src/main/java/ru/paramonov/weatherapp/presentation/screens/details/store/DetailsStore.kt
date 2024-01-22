package ru.paramonov.weatherapp.presentation.screens.details.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.entity.Forecast

interface DetailsStore : Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Intent {

        data object ClickToBack : Intent

        data object ClickChangeFavoriteStatus : Intent
    }

    sealed interface Label {

        data object ClickToBack : Label
    }
}
