package ru.paramonov.weatherapp.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.paramonov.weatherapp.BuildConfig
import ru.paramonov.weatherapp.data.network.api.ApiService
import ru.paramonov.weatherapp.di.annotation.ApplicationScope

@Module
object NetworkModule {

    private const val KEY_PARAM = "key"

    @[ApplicationScope Provides]
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @[ApplicationScope Provides]
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor { chain ->
            val currentRequest = chain.request()

            val modifiedUrl = currentRequest.url.newBuilder()
                .addQueryParameter(name = KEY_PARAM, value = BuildConfig.WEATHER_API_KEY)
                .build()

            val modifiedRequest = currentRequest.newBuilder()
                .url(url = modifiedUrl)
                .build()

            chain.proceed(request = modifiedRequest)
        }.build()

    @[ApplicationScope Provides]
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.weatherapi.com/v1/")
        .build()

    @[ApplicationScope Provides]
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService = retrofit.create()
}
