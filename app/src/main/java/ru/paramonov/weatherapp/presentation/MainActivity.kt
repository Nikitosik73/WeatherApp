package ru.paramonov.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.data.network.api.ApiFactory
import ru.paramonov.weatherapp.presentation.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = ApiFactory.apiService
        CoroutineScope(Dispatchers.Main).launch {
            val forecast = api.loadForecastWeather(query = "Moscow")
            Log.d("MainActivity", forecast.toString())
        }

        setContent {
            WeatherAppTheme {

            }
        }
    }
}