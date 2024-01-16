package ru.paramonov.weatherapp.presentation.screens.search.store

import com.arkivanov.mvikotlin.core.store.Store

interface SearchStore : Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> {

    data class State(val todo: Unit)

    sealed interface Intent {

    }

    sealed interface Label {

    }
}