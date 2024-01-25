package ru.paramonov.weatherapp.presentation.screens.root.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.paramonov.weatherapp.presentation.screens.details.component.DetailsComponent
import ru.paramonov.weatherapp.presentation.screens.favorite.component.FavoriteComponent
import ru.paramonov.weatherapp.presentation.screens.search.component.SearchComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Favorite(val component: FavoriteComponent): Child

        data class Details(val component: DetailsComponent): Child

        data class Search(val component: SearchComponent): Child
    }
}
