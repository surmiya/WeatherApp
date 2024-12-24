package com.nooro.weatherapp.domain.repository

import com.nooro.weatherapp.domain.model.City
import com.nooro.weatherapp.domain.model.Locations
import com.nooro.weatherapp.domain.model.WeatherInfoPerCity
import com.nooro.weatherapp.domain.model.WeatherSummary
import com.nooro.weatherapp.domain.util.Resource

interface WeatherRepository {
    suspend fun getWeatherData(location: String): Resource<WeatherSummary>
    suspend fun getWeatherDataForMultipleCities(location: Locations): Resource<WeatherInfoPerCity>
    suspend fun searchCities(city: String): Resource<List<City>>
}