package ru.paramonov.weatherapp.presentation.screens.search.component.componentdefault

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.extensions.componentScope
import ru.paramonov.weatherapp.presentation.screens.search.OpenReason
import ru.paramonov.weatherapp.presentation.screens.search.component.SearchComponent
import ru.paramonov.weatherapp.presentation.screens.search.store.SearchStore
import ru.paramonov.weatherapp.presentation.screens.search.store.storefactory.SearchStoreFactory

class DefaultSearchComponent @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onForecastForCityRequested") private val onForecastForCityRequested: (City) -> Unit,
    @Assisted("onSavedTaFavorite") private val onSavedTaFavorite: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
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

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onForecastForCityRequested") onForecastForCityRequested: (City) -> Unit,
            @Assisted("onSavedTaFavorite") onSavedTaFavorite: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultSearchComponent
    }
}