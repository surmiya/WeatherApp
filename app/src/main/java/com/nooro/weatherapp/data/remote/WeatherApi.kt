package com.nooro.weatherapp.data.remote

import com.nooro.weatherapp.domain.model.City
import com.nooro.weatherapp.domain.model.Locations
import com.nooro.weatherapp.domain.model.WeatherSummary
import com.nooro.weatherapp.domain.model.WeatherInfoPerCity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/current.json")
    suspend fun getWeatherData(
        @Query("q") location: String
    ): WeatherSummary

    @POST("v1/current.json?q=bulk")
    suspend fun getWeatherDataForMultipleCities(
        @Body locationRequest: Locations
    ): WeatherInfoPerCity

    @GET("v1/search.json")
    suspend fun searchCities(
        @Query("q") city: String
    ): List<City>
}