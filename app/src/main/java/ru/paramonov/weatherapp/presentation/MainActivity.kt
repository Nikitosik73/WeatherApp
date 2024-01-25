package ru.paramonov.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.data.network.api.ApiFactory
import ru.paramonov.weatherapp.presentation.application.WeatherApplication
import ru.paramonov.weatherapp.presentation.screens.root.component.componentdefault.DefaultRootComponent
import ru.paramonov.weatherapp.presentation.screens.root.content.RootContent
import ru.paramonov.weatherapp.presentation.ui.theme.WeatherAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private val component by lazy {
        (applicationContext as WeatherApplication).component
    }

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(activity = this)
        super.onCreate(savedInstanceState)

        val rootComponent =
            rootComponentFactory.create(componentContext = defaultComponentContext())

        setContent {
            RootContent(component = rootComponent)
        }
    }
}