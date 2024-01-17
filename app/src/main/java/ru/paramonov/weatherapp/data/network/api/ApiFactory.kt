package ru.paramonov.weatherapp.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.paramonov.weatherapp.BuildConfig

object ApiFactory {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val KEY_PARAM = "key"

    private val okHttpLoggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor = okHttpLoggingInterceptor)
        .addInterceptor { chain ->
            val currentRequest = chain.request()

            val modifiedUrl = currentRequest.url.newBuilder()
                .addQueryParameter(name = KEY_PARAM, value = BuildConfig.WEATHER_API_KEY)
                .build()

            val modifiedRequest = currentRequest.newBuilder()
                .url(url = modifiedUrl)
                .build()

            chain.proceed(request = modifiedRequest)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create()
}
