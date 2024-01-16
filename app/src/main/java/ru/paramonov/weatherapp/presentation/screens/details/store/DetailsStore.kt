package ru.paramonov.weatherapp.presentation.screens.details.store

import com.arkivanov.mvikotlin.core.store.Store

interface DetailsStore : Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {

    data class State(val todo: Unit)

    sealed interface Intent {

    }

    sealed interface Label {

    }
}
