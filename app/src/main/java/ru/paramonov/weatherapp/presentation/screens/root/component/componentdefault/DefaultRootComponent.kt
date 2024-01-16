package ru.paramonov.weatherapp.presentation.screens.root.component.componentdefault

import com.arkivanov.decompose.ComponentContext
import ru.paramonov.weatherapp.presentation.screens.root.component.RootComponent

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

}