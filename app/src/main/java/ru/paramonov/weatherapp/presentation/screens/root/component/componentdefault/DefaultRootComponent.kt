package ru.paramonov.weatherapp.presentation.screens.root.component.componentdefault

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.screens.details.component.componentdefault.DefaultDetailsComponent
import ru.paramonov.weatherapp.presentation.screens.favorite.component.componentdefault.DefaultFavoriteComponent
import ru.paramonov.weatherapp.presentation.screens.root.component.RootComponent
import ru.paramonov.weatherapp.presentation.screens.search.OpenReason
import ru.paramonov.weatherapp.presentation.screens.search.component.componentdefault.DefaultSearchComponent
import javax.inject.Inject

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favorite,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.Details -> {
            val component = detailsComponentFactory.create(
                city = config.city,
                onBackClicked = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            RootComponent.Child.Details(component = component)
        }

        Config.Favorite -> {
            val component = favoriteComponentFactory.create(
                onClickedCityItem = {
                    navigation.push(configuration = Config.Details(city = it))
                },
                onClickedAddToFavorite = {
                    navigation.push(configuration = Config.Search(openReason = OpenReason.AddToFavorite))
                },
                onClickedToSearch = {
                    navigation.push(configuration = Config.Search(openReason = OpenReason.RegularSearch))
                },
                componentContext = componentContext
            )
            RootComponent.Child.Favorite(component = component)
        }

        is Config.Search -> {
            val component = searchComponentFactory.create(
                openReason = config.openReason,
                onBackClicked = {
                    navigation.pop()
                },
                onForecastForCityRequested = {
                    navigation.push(configuration = Config.Details(city = it))
                },
                onSavedTaFavorite = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            RootComponent.Child.Search(component = component)
        }
    }


    sealed interface Config : Parcelable {

        @Parcelize
        data object Favorite : Config

        @Parcelize
        data class Search(val openReason: OpenReason) : Config

        @Parcelize
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}