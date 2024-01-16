package ru.paramonov.weatherapp.presentation.screens.details.store.storefactory

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.paramonov.weatherapp.presentation.screens.details.store.DetailsStore

class DetailsStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): DetailsStore =
        object : DetailsStore, Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> by
        storeFactory.create(
            name = "DetailsStore",
            initialState = DetailsStore.State(Unit),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl
        ) {}

    private sealed interface Action {

    }

    private sealed interface Message {

    }

    private inner class ExecutorImpl : CoroutineExecutor<DetailsStore.Intent, Action,
            DetailsStore.State, Message, DetailsStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> DetailsStore.State
        ) {

        }

        override fun executeIntent(
            intent: DetailsStore.Intent,
            getState: () -> DetailsStore.State
        ) {

        }
    }


    private object ReducerImpl : Reducer<DetailsStore.State, Message> {

        override fun DetailsStore.State.reduce(
            msg: Message
        ): DetailsStore.State = DetailsStore.State(Unit)
    }
}