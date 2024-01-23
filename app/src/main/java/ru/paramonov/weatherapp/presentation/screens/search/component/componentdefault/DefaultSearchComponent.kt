package ru.paramonov.weatherapp.presentation.screens.search.component.componentdefault

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.extensions.componentScope
import ru.paramonov.weatherapp.presentation.screens.search.OpenReason
import ru.paramonov.weatherapp.presentation.screens.search.component.SearchComponent
import ru.paramonov.weatherapp.presentation.screens.search.store.SearchStore
import ru.paramonov.weatherapp.presentation.screens.search.store.storefactory.SearchStoreFactory

class DefaultSearchComponent(
    private val openReason: OpenReason,
    private val searchStoreFactory: SearchStoreFactory,
    private val onBackClicked: () -> Unit,
    private val onForecastForCityRequested: (City) -> Unit,
    private val onSavedTaFavorite: () -> Unit,
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        searchStoreFactory.create(openReason = openReason)
    }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { searchLabels ->
                when(searchLabels) {
                    SearchStore.Label.ClickToBack -> onBackClicked()
                    is SearchStore.Label.OpenForecast -> onForecastForCityRequested(searchLabels.city)
                    SearchStore.Label.SavedToFavorite -> onSavedTaFavorite()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun onClickToBack() {
        store.accept(intent = SearchStore.Intent.ClickToBack)
    }

    override fun onClickToSearch() {
        store.accept(intent = SearchStore.Intent.ClickToSearch)
    }

    override fun onClickToCityItem(city: City) {
        store.accept(intent = SearchStore.Intent.ClickToCity(city = city))
    }

    override fun changeSearchQuery(query: String) {
        store.accept(intent = SearchStore.Intent.ChangeSearchQuery(query = query))
    }
}