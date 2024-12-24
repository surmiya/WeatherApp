package com.nooro.weatherapp.domain.model

data class Locations(
    val locations: List<Loc>
)

data class Loc(
    val q: String,
    val custom_id: String
)
