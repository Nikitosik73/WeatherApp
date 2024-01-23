package ru.paramonov.weatherapp.presentation.screens.favorite.store.storefactory

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import ru.paramonov.weatherapp.domain.usecase.GetFavoriteCitiesUseCase
import ru.paramonov.weatherapp.presentation.screens.favorite.store.FavoriteStore
import javax.inject.Inject

class FavoriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) {

    fun create(): FavoriteStore = object : FavoriteStore, Store<FavoriteStore.Intent,
            FavoriteStore.State, FavoriteStore.Label> by storeFactory.create(
        name = "FavoriteStore",
        initialState = FavoriteStore.State(cityItems = listOf()),
        reducer = ReducerImpl,
        executorFactory = ::ExecutorImpl,
        bootstrapper = BootstrapperImpl()
    ) {}

    private sealed interface Action {

        data class FavoriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Message {

        data class FavoriteCitiesLoaded(val cities: List<City>) : Message

        data class WeatherIsLoading(val cityId: Int) : Message

        data class WeatherLoadedError(val cityId: Int) : Message

        data class WeatherIsLoaded(
            val cityId: Int,
            val temperatureC: Float,
            val conditionImageUrl: String
        ) : Message
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect { cities ->
                    dispatch(
                        action = Action.FavoriteCitiesLoaded(
                            cities = cities
                        )
                    )
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<FavoriteStore.Intent, Action,
            FavoriteStore.State, Message, FavoriteStore.Label>() {
        override fun executeAction(
            action: Action,
            getState: () -> FavoriteStore.State
        ) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(message = Message.FavoriteCitiesLoaded(cities = cities))
                    cities.forEach { city ->
                        scope.launch {
                            loadWeatherForCity(
                                city = city,
                                scope = scope
                            )
                        }
                    }
                }
            }
        }

        override fun executeIntent(
            intent: FavoriteStore.Intent,
            getState: () -> FavoriteStore.State
        ) {
            when (intent) {
                is FavoriteStore.Intent.CityItemClicked -> {
                    publish(label = FavoriteStore.Label.CityItemClicked(city = intent.city))
                }

                FavoriteStore.Intent.ClickAddToFavorite -> {
                    publish(label = FavoriteStore.Label.ClickAddToFavorite)
                }

                FavoriteStore.Intent.ClickToSearch -> {
                    publish(label = FavoriteStore.Label.ClickToSearch)
                }
            }
        }

        private fun loadWeatherForCity(
            city: City,
            scope: CoroutineScope
        ) {
            scope.launch {
                dispatch(message = Message.WeatherIsLoading(cityId = city.id))
                val weather = getCurrentWeatherUseCase(cityId = city.id)
                dispatch(
                    message = Message.WeatherIsLoaded(
                        cityId = city.id,
                        temperatureC = weather.temperatureC,
                        conditionImageUrl = weather.conditionImageUrl
                    )
                )
            }.invokeOnCompletion { cause: Throwable? ->
                cause?.let {
                    dispatch(message = Message.WeatherLoadedError(cityId = city.id))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<FavoriteStore.State, Message> {

        override fun FavoriteStore.State.reduce(
            msg: Message
        ): FavoriteStore.State = when (msg) {
            is Message.FavoriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map { city ->
                        FavoriteStore.State.CityItem(
                            city = city,
                            weatherState = FavoriteStore.State.WeatherState.Initial
                        )
                    }
                )
            }

            is Message.WeatherIsLoaded -> {
                copy(
                    cityItems = cityItems.map { cityItem ->
                        if (cityItem.city.id == msg.cityId) {
                            cityItem.copy(
                                weatherState = FavoriteStore.State.WeatherState.Loaded(
                                    temperatureC = msg.temperatureC,
                                    conditionImageUrl = msg.conditionImageUrl
                                )
                            )
                        } else {
                            cityItem
                        }
                    }
                )
            }

            is Message.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map { cityItem ->
                        if (cityItem.city.id == msg.cityId) {
                            cityItem.copy(weatherState = FavoriteStore.State.WeatherState.Loading)
                        } else {
                            cityItem
                        }
                    }
                )
            }

            is Message.WeatherLoadedError -> {
                copy(
                    cityItems = cityItems.map { cityItem ->
                        if (cityItem.city.id == msg.cityId) {
                            cityItem.copy(weatherState = FavoriteStore.State.WeatherState.Error)
                        } else {
                            cityItem
                        }
                    }
                )
            }
        }
    }
}