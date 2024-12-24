package com.nooro.weatherapp.di

import com.nooro.weatherapp.data.remote.WeatherApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .client(provideOkHttpClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                )
            )
            .build()
            .create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .addInterceptor(
                Interceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl: HttpUrl = original.url
                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("key", "465bfff3676d4fc989f203640242212")
                        .build()

                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder().url(url)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
            )
            .build()
    }
}