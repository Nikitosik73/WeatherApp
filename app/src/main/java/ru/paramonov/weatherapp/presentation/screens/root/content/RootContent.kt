package ru.paramonov.weatherapp.presentation.screens.root.content

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.paramonov.weatherapp.presentation.screens.details.content.DetailsContent
import ru.paramonov.weatherapp.presentation.screens.favorite.content.FavoriteContent
import ru.paramonov.weatherapp.presentation.screens.root.component.RootComponent
import ru.paramonov.weatherapp.presentation.screens.search.content.SearchContent
import ru.paramonov.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent) {

    WeatherAppTheme {
        Children(
            stack = component.childStack
        ) {
            when(val instance = it.instance) {
                is RootComponent.Child.Details -> DetailsContent(component = instance.component)
                is RootComponent.Child.Favorite -> FavoriteContent(component = instance.component)
                is RootComponent.Child.Search -> SearchContent(component = instance.component)
            }
        }
    }
}