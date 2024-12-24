package com.nooro.weatherapp.presentation

import com.nooro.weatherapp.domain.model.WeatherSummary
import com.nooro.weatherapp.domain.model.WeatherInfoPerCity

sealed class WeatherState {
    data object Loading : WeatherState()
    data object Empty : WeatherState()
    data object Error : WeatherState()

    data class Data (
        val weatherSummary: WeatherSummary? = null,
        val weatherInfoPerCity: WeatherInfoPerCity? = null,
    ) : WeatherState()
}