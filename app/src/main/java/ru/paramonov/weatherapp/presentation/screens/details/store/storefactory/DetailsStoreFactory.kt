package ru.paramonov.weatherapp.presentation.screens.details.store.storefactory

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.entity.Forecast
import ru.paramonov.weatherapp.domain.usecase.ChangeFavoriteStateUseCase
import ru.paramonov.weatherapp.domain.usecase.GetForecastUseCase
import ru.paramonov.weatherapp.domain.usecase.ObserveFavoriteStateUseCase
import ru.paramonov.weatherapp.presentation.screens.details.store.DetailsStore
import javax.inject.Inject

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val observeFavoriteStateUseCase: ObserveFavoriteStateUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> by
        storeFactory.create(
            name = "DetailsStore",
            initialState = DetailsStore.State(
                city = city,
                isFavorite = false,
                DetailsStore.State.ForecastState.Initial
            ),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl,
            bootstrapper = BootstrapperImpl(city = )
        ) {}

    private sealed interface Action {

        data class FavoriteChangeStatus(val isFavorite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastIsLoading : Action

        data object ForecastLoadingError : Action
    }

    private sealed interface Message {

        data class FavoriteChangeStatus(val isFavorite: Boolean) : Message

        data class ForecastLoaded(val forecast: Forecast) : Message

        data object ForecastIsLoading : Message

        data object ForecastLoadingError : Message
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                observeFavoriteStateUseCase(cityId = city.id).collect { isFavorite ->
                    dispatch(action = Action.FavoriteChangeStatus(isFavorite = isFavorite))
                }
            }
            scope.launch {
                dispatch(action = Action.ForecastIsLoading)
                val forecast = getForecastUseCase(cityId = city.id)
                dispatch(action = Action.ForecastLoaded(forecast = forecast))
            }.invokeOnCompletion { cause: Throwable? ->
                cause?.let {
                    dispatch(action = Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<DetailsStore.Intent, Action,
            DetailsStore.State, Message, DetailsStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> DetailsStore.State
        ) {
            when(action) {
                is Action.FavoriteChangeStatus -> {
                    dispatch(message = Message.FavoriteChangeStatus(isFavorite = action.isFavorite))
                }
                Action.ForecastIsLoading -> {
                    dispatch(message = Message.ForecastIsLoading)
                }
                is Action.ForecastLoaded -> {
                    dispatch(message = Message.ForecastLoaded(forecast = action.forecast))
                }
                Action.ForecastLoadingError -> {
                    dispatch(message = Message.ForecastLoadingError)
                }
            }
        }

        override fun executeIntent(
            intent: DetailsStore.Intent,
            getState: () -> DetailsStore.State
        ) {
            when(intent) {
                DetailsStore.Intent.ClickChangeFavoriteStatus -> {
                    scope.launch {
                        val currentState = getState()
                        if (currentState.isFavorite) {
                            changeFavoriteStateUseCase.removeFromFavorite(cityId = currentState.city.id)
                        } else {
                            changeFavoriteStateUseCase.addToFavorite(city = currentState.city)
                        }
                    }
                }
                DetailsStore.Intent.ClickToBack -> {
                    publish(label = DetailsStore.Label.ClickToBack)
                }
            }
        }
    }


    private object ReducerImpl : Reducer<DetailsStore.State, Message> {

        override fun DetailsStore.State.reduce(
            msg: Message
        ): DetailsStore.State = when(msg) {
            is Message.FavoriteChangeStatus -> {
                copy(isFavorite = msg.isFavorite)
            }
            Message.ForecastIsLoading -> {
                copy(forecastState = DetailsStore.State.ForecastState.Loading)
            }
            is Message.ForecastLoaded -> {
                copy(forecastState = DetailsStore.State.ForecastState.Loaded(forecast = msg.forecast))
            }
            Message.ForecastLoadingError -> {
                copy(forecastState = DetailsStore.State.ForecastState.Error)
            }
        }
    }
}