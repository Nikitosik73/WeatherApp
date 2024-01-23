package ru.paramonov.weatherapp.presentation.screens.details.component

import kotlinx.coroutines.flow.StateFlow
import ru.paramonov.weatherapp.presentation.screens.details.store.DetailsStore

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun onClickToBack()

    fun onClickChangeFavoriteStatus()
}
