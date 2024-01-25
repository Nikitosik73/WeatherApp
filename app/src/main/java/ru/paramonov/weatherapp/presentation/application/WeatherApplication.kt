package ru.paramonov.weatherapp.presentation.application

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.paramonov.weatherapp.di.component.ApplicationComponent
import ru.paramonov.weatherapp.di.component.DaggerApplicationComponent

class WeatherApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(context = this)
    }
}
