package ru.paramonov.weatherapp.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.math.roundToInt

fun ComponentContext.componentScope(): CoroutineScope = CoroutineScope(
    context = Dispatchers.Main.immediate
).apply {
    lifecycle.doOnDestroy { cancel() }
}

fun Float.temperatureFormattedToString(): String = "${roundToInt()}°C"