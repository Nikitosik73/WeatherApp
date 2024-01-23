package ru.paramonov.weatherapp.presentation.screens.favorite.component.componentdefault

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.extensions.componentScope
import ru.paramonov.weatherapp.presentation.screens.favorite.component.FavoriteComponent
import ru.paramonov.weatherapp.presentation.screens.favorite.store.FavoriteStore
import ru.paramonov.weatherapp.presentation.screens.favorite.store.storefactory.FavoriteStoreFactory
import javax.inject.Inject

class DefaultFavoriteComponent @Inject constructor(
    private val favoriteStoreFactory: FavoriteStoreFactory,
    private val onClickedCityItem: (City) -> Unit,
    private val onClickedAddToFavorite: () -> Unit,
    private val onClickedToSearch: () -> Unit,
    componentContext: ComponentContext
) : FavoriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { favoriteStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { favoriteLabels ->
                when(favoriteLabels) {
                    is FavoriteStore.Label.CityItemClicked -> onClickedCityItem(favoriteLabels.city)
                    FavoriteStore.Label.ClickAddToFavorite -> onClickedAddToFavorite()
                    FavoriteStore.Label.ClickToSearch -> onClickedToSearch()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    override fun onClickToSearch() {
        store.accept(intent = FavoriteStore.Intent.ClickToSearch)
    }

    override fun onClickAddToFavorite() {
        store.accept(intent = FavoriteStore.Intent.ClickAddToFavorite)
    }

    override fun onClickCityItem(city: City) {
        store.accept(intent = FavoriteStore.Intent.CityItemClicked(city = city))
    }
}