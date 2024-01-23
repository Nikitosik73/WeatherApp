package ru.paramonov.weatherapp.presentation.screens.search.store.storefactory

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.usecase.ChangeFavoriteStateUseCase
import ru.paramonov.weatherapp.domain.usecase.SearchCityUseCase
import ru.paramonov.weatherapp.presentation.screens.details.store.storefactory.DetailsStoreFactory
import ru.paramonov.weatherapp.presentation.screens.search.OpenReason
import ru.paramonov.weatherapp.presentation.screens.search.store.SearchStore
import javax.inject.Inject

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase
) {

    fun create(openReason: OpenReason): SearchStore = object : SearchStore, Store<SearchStore.Intent, SearchStore.State,
            SearchStore.Label> by storeFactory.create(
                name = "SearchStore",
                initialState = SearchStore.State(
                    searchQuery = "",
                    searchState = SearchStore.State.SearchState.Initial
                ),
                reducer = ReducerImpl,
                executorFactory = { ExecutorImpl(openReason = openReason) }
            ) {}

    private sealed interface Action

    private sealed interface Message {

        data class ChangeSearchQuery(val query: String) : Message

        data object LoadingSearchResult : Message

        data object SearchResultError : Message

        data class SearchResultLoaded(val cities: List<City>) : Message
    }

    private inner class ExecutorImpl(
        val openReason: OpenReason
    ) : CoroutineExecutor<SearchStore.Intent, Action, SearchStore.State, Message, SearchStore.Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(
            intent: SearchStore.Intent,
            getState: () -> SearchStore.State
        ) {
            when(intent) {
                is SearchStore.Intent.ChangeSearchQuery -> {
                    dispatch(message = Message.ChangeSearchQuery(query = intent.query))
                }
                SearchStore.Intent.ClickToBack -> {
                    publish(label = SearchStore.Label.ClickToBack)
                }
                is SearchStore.Intent.ClickToCity -> {
                    when(openReason) {
                        OpenReason.AddToFavorite -> {
                            scope.launch {
                                changeFavoriteStateUseCase.addToFavorite(city = intent.city)
                                publish(label = SearchStore.Label.SavedToFavorite)
                            }
                        }
                        OpenReason.RegularSearch -> {
                            publish(SearchStore.Label.OpenForecast(city = intent.city))
                        }
                    }
                }
                SearchStore.Intent.ClickToSearch -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(message = Message.LoadingSearchResult)
                        try {
                            val cities = searchCityUseCase(query = getState().searchQuery)
                            dispatch(message = Message.SearchResultLoaded(cities = cities))
                        } catch (e: Exception) {
                            dispatch(message = Message.SearchResultError)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<SearchStore.State, Message> {

        override fun SearchStore.State.reduce(
            msg: Message
        ): SearchStore.State = when(msg) {
            is Message.ChangeSearchQuery -> {
                copy(searchQuery = msg.query)
            }
            Message.LoadingSearchResult -> {
                copy(searchState = SearchStore.State.SearchState.Loading)
            }
            Message.SearchResultError -> {
                copy(searchState = SearchStore.State.SearchState.Error)
            }
            is Message.SearchResultLoaded -> {
                val searchState = if (msg.cities.isEmpty()) {
                    SearchStore.State.SearchState.EmptyResult
                } else {
                    SearchStore.State.SearchState.SuccessResult(cities = msg.cities)
                }
                copy(searchState = searchState)
            }
        }
    }
}