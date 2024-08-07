package ru.paramonov.weatherapp.presentation.screens.favorite.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ru.paramonov.weatherapp.R
import ru.paramonov.weatherapp.presentation.extensions.temperatureFormattedToString
import ru.paramonov.weatherapp.presentation.screens.favorite.component.FavoriteComponent
import ru.paramonov.weatherapp.presentation.screens.favorite.store.FavoriteStore
import ru.paramonov.weatherapp.presentation.ui.theme.CardGradients
import ru.paramonov.weatherapp.presentation.ui.theme.Gradient
import ru.paramonov.weatherapp.presentation.ui.theme.Orange

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
        item(
            span = { GridItemSpan(currentLineSpan = 2) }
        ) {
            SearchCard(onClick = { component.onClickToSearch() })
        }
        itemsIndexed(
            items = state.cityItems,
            key = { _, cityItem -> cityItem.city.id }
        ) { index, cityItem ->
            CardCity(
                cityItem = cityItem,
                index = index,
                onClick = { component.onClickCityItem(city = cityItem.city) })
        }
        item {
            AddFavoriteCityCard(onClick = { component.onClickAddToFavorite() })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CardCity(
    cityItem: FavoriteStore.State.CityItem,
    index: Int,
    onClick: () -> Unit
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
                .clickable { onClick() }
                .padding(all = 24.dp)
        ) {
            when (val weatherState = cityItem.weatherState) {
                FavoriteStore.State.WeatherState.Error -> {
                    Icon(
                        imageVector = Icons.Rounded.Error,
                        contentDescription = "Error",
                        modifier = Modifier
                            .size(size = 56.dp)
                            .align(alignment = Alignment.Center),
                        tint = Color.Red,
                    )
                }

                FavoriteStore.State.WeatherState.Initial -> {}
                is FavoriteStore.State.WeatherState.Loaded -> {
                    GlideImage(
                        model = weatherState.conditionImageUrl,
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .size(size = 56.dp)
                    )

                    Text(
                        text = weatherState.temperatureC.temperatureFormattedToString(),
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp),
                        modifier = Modifier
                            .align(alignment = Alignment.BottomStart)
                            .padding(bottom = 24.dp)
                    )
                }

                FavoriteStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
            Text(
                text = cityItem.city.name,
                modifier = Modifier.align(alignment = Alignment.BottomStart),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun AddFavoriteCityCard(
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .sizeIn(minHeight = 196.dp)
                .fillMaxWidth()
                .padding(all = 24.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Add Favorite",
                tint = Orange,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(all = 16.dp)
                    .size(size = 48.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.button_add_favorite),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SearchCard(
    onClick: () -> Unit
) {
    val gradient = CardGradients.gradients[3]
    Card(
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .background(brush = gradient.primaryGradient)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search City",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.search),
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(all = 16.dp)
            )
        }
    }
}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients: List<Gradient> = CardGradients.gradients
    return gradients[index % gradients.size]
}
