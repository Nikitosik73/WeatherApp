package ru.paramonov.weatherapp.presentation.screens.favorite.store.storefactory

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.paramonov.weatherapp.presentation.screens.favorite.store.FavoriteStore

class FavoriteStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): FavoriteStore = object : FavoriteStore, Store<FavoriteStore.Intent,
            FavoriteStore.State, FavoriteStore.Label> by storeFactory.create(
                name = "FavoriteStore",
                initialState = FavoriteStore.State(Unit),
                reducer = ReducerImpl,
                executorFactory = ::ExecutorImpl
            ) {}

    private sealed interface Action {

    }

    private sealed interface Message {

    }

    private inner class ExecutorImpl : CoroutineExecutor<FavoriteStore.Intent, Action,
            FavoriteStore.State, Message, FavoriteStore.Label>() {
        override fun executeAction(
            action: Action,
            getState: () -> FavoriteStore.State
        ) {

        }

        override fun executeIntent(
            intent: FavoriteStore.Intent,
            getState: () -> FavoriteStore.State
        ) {

        }
    }

    private object ReducerImpl : Reducer<FavoriteStore.State, Message> {

        override fun FavoriteStore.State.reduce(
            msg: Message
        ): FavoriteStore.State = FavoriteStore.State(Unit)
    }
}