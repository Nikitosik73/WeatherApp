package ru.paramonov.weatherapp.presentation.screens.search.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.paramonov.weatherapp.domain.entity.City

interface SearchStore : Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> {

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState{

            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class SuccessResult(val cities: List<City>) : SearchState
        }
    }

    sealed interface Intent {

        data object ClickToBack : Intent

        data object ClickToSearch : Intent

        data class ClickToCity(val city: City) : Intent

        data class ChangeSearchQuery(val query: String) : Intent
    }

    sealed interface Label {

        data object ClickToBack : Label

        data object SavedToFavorite : Label

        data class OpenForecast(val city: City) : Label
    }
}