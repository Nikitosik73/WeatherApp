package ru.paramonov.weatherapp.presentation.screens.search.component

import kotlinx.coroutines.flow.StateFlow
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.screens.search.store.SearchStore

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun onClickToBack()

    fun onClickToSearch()

    fun onClickToCityItem(city: City)

    fun changeSearchQuery(query: String)
}
