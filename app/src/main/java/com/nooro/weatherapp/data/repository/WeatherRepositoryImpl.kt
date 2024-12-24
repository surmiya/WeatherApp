package com.nooro.weatherapp.data.repository

import com.nooro.weatherapp.data.remote.WeatherApi
import com.nooro.weatherapp.domain.model.City
import com.nooro.weatherapp.domain.model.Locations
import com.nooro.weatherapp.domain.model.WeatherSummary
import com.nooro.weatherapp.domain.model.WeatherInfoPerCity
import com.nooro.weatherapp.domain.repository.WeatherRepository
import com.nooro.weatherapp.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getWeatherData(location: String): Resource<WeatherSummary> {
        return try {
            Resource.Success(
                data = api.getWeatherData(location)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getWeatherDataForMultipleCities(location: Locations): Resource<WeatherInfoPerCity> {
        return try {
            Resource.Success(
                data = api.getWeatherDataForMultipleCities(location)
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
}

    override suspend fun searchCities(city: String): Resource<List<City>> {
        return try {
            Resource.Success(
                data = api.searchCities(city)
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}