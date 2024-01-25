package ru.paramonov.weatherapp.presentation.screens.favorite.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.paramonov.weatherapp.presentation.screens.favorite.component.FavoriteComponent
import ru.paramonov.weatherapp.presentation.screens.favorite.store.FavoriteStore
import ru.paramonov.weatherapp.presentation.ui.theme.CardGradients
import ru.paramonov.weatherapp.presentation.ui.theme.Gradient

@Composable
fun FavoriteContent(component: FavoriteComponent) {

    val state by component.model.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        itemsIndexed(
            items = state.cityItems,
            key = { _, cityItem -> cityItem.city.id }
        ) { index, cityItem ->
            CardCity(cityItem = cityItem, index = index)
        }
    }
}

@Composable
private fun CardCity(
    cityItem: FavoriteStore.State.CityItem,
    index: Int
) {
    val gradient: Gradient = getGradientByIndex(index = index)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                shape = MaterialTheme.shapes.extraLarge,
                spotColor = gradient.shadowColor
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.Blue)
    ) {
        Box(
            modifier = Modifier
                .background(brush = gradient.primaryGradient)
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        ),
                        radius = size.maxDimension / 2
                    )
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x + size.width / 2,
                            y = center.y - size.height / 2
                        ),
                        radius = size.maxDimension / 2
                    )
                }
                .padding(all = 24.dp)
        ) {
            Text(
                text = cityItem.city.name,
                modifier = Modifier.align(alignment = Alignment.BottomStart),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients: List<Gradient> = CardGradients.gradients
    return gradients[index % gradients.size]
}
