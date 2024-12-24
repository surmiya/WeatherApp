package com.nooro.weatherapp.domain.model

data class WeatherSummary(
    val location: Location,
    val current: WeatherData
)

data class WeatherInfoPerCity(
    val bulk: List<BulkInfo>?
)

data class BulkInfo(
    val query : WeatherSummary,
)

data class Location(
    val name : String,
    val country : String,
    val lat: Double,
    val lon: Double
)

data class WeatherData(
    val temp_c: Double,
    val feelslike_c: Double,
    val humidity: Int,
    val uv: Double,
    val condition: Condition
)

data class Condition(
    val icon: String,
)
