package ru.paramonov.weatherapp.presentation.screens.search.store.storefactory

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.paramonov.weatherapp.presentation.screens.search.store.SearchStore

class SearchStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): SearchStore = object : SearchStore, Store<SearchStore.Intent, SearchStore.State,
            SearchStore.Label> by storeFactory.create(
                name = "SearchStore",
                initialState = SearchStore.State(Unit),
                reducer = ReducerImpl,
                executorFactory = SearchStoreFactory::ExecutorImpl
            ) {}

    private sealed interface Action {

    }

    private sealed interface Message {

    }

    private inner class ExecutorImpl : CoroutineExecutor<SearchStore.Intent, Action,
            SearchStore.State, Message, SearchStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> SearchStore.State
        ) {

        }

        override fun executeIntent(
            intent: SearchStore.Intent,
            getState: () -> SearchStore.State
        ) {

        }
    }

    private object ReducerImpl : Reducer<SearchStore.State, Message> {

        override fun SearchStore.State.reduce(
            msg: Message
        ): SearchStore.State = SearchStore.State(Unit)
    }
}