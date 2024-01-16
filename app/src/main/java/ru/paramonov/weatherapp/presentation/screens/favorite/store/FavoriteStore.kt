package ru.paramonov.weatherapp.presentation.screens.favorite.store

import com.arkivanov.mvikotlin.core.store.Store

interface FavoriteStore : Store<FavoriteStore.Intent, FavoriteStore.State, FavoriteStore.Label> {

    data class State(val todo: Unit)

    sealed interface Intent {

    }

    sealed interface Label {

    }
}
