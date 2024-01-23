package ru.paramonov.weatherapp.presentation.screens.details.component.componentdefault

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.paramonov.weatherapp.domain.entity.City
import ru.paramonov.weatherapp.presentation.extensions.componentScope
import ru.paramonov.weatherapp.presentation.screens.details.component.DetailsComponent
import ru.paramonov.weatherapp.presentation.screens.details.store.DetailsStore
import ru.paramonov.weatherapp.presentation.screens.details.store.storefactory.DetailsStoreFactory
import javax.inject.Inject

class DefaultDetailsComponent @AssistedInject constructor(
    private val detailsStoreFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city = city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {detailLabel ->
                when(detailLabel) {
                    DetailsStore.Label.ClickToBack -> onBackClicked()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickToBack() {
        store.accept(intent = DetailsStore.Intent.ClickToBack)
    }

    override fun onClickChangeFavoriteStatus() {
        store.accept(intent = DetailsStore.Intent.ClickChangeFavoriteStatus)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("city") city: City,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}