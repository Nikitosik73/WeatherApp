package ru.paramonov.weatherapp.di.module

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.paramonov.weatherapp.data.local.db.AppDatabase
import ru.paramonov.weatherapp.data.local.db.dao.FavoriteCitiesDao
import ru.paramonov.weatherapp.data.repository.FavoriteRepositoryImpl
import ru.paramonov.weatherapp.data.repository.SearchRepositoryImpl
import ru.paramonov.weatherapp.data.repository.WeatherRepositoryImpl
import ru.paramonov.weatherapp.di.annotation.ApplicationScope
import ru.paramonov.weatherapp.domain.repository.FavoriteRepository
import ru.paramonov.weatherapp.domain.repository.SearchRepository
import ru.paramonov.weatherapp.domain.repository.WeatherRepository

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @[ApplicationScope Binds]
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideAppDatabase(context: Context): AppDatabase =
            AppDatabase.getInstance(context = context)

        @[ApplicationScope Provides]
        fun provideFavoriteCitiesDao(database: AppDatabase): FavoriteCitiesDao =
            database.favoriteCitiesDao()
    }
}
