package com.nooro.weatherapp.presentation

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nooro.weatherapp.domain.model.Loc
import com.nooro.weatherapp.domain.model.Locations
import com.nooro.weatherapp.domain.model.WeatherSummary
import com.nooro.weatherapp.domain.repository.WeatherRepository
import com.nooro.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val repository: WeatherRepository
): ViewModel() {

    private var _uiState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    internal val uiState: StateFlow<WeatherState>
        get() = _uiState

    internal var textFieldState = mutableStateOf(TextFieldValue(text = ""))

    internal fun loadWeatherInfo() {
        val location = getCurrentCity()
        if (location.isEmpty()) {
            _uiState.update {
                WeatherState.Empty
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                WeatherState.Loading
            }

            when (val result = repository.getWeatherData(location)) {
                is Resource.Success -> {
                    _uiState.update {
                        WeatherState.Data(weatherSummary = result.data)
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        WeatherState.Empty
                    }
                }
            }
        }
    }

    private fun loadWeatherInfoForMultipleCities(locations: Locations) {
        viewModelScope.launch {
            _uiState.update {
                WeatherState.Loading
            }

            when (val result = repository.getWeatherDataForMultipleCities(locations)) {
                is Resource.Success -> {
                  _uiState.update {
                        WeatherState.Data(weatherInfoPerCity = result.data)
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        WeatherState.Error
                    }
                }
            }
        }
    }

    internal fun searchCities(city: String) {
        viewModelScope.launch {
            _uiState.update {
                WeatherState.Loading
            }

            when (val result = repository.searchCities(city)) {
                is Resource.Success -> {
                    val locations = mutableListOf<Loc>()
                    result.data?.forEachIndexed {  index, it  ->
                        locations.add(Loc("${it.lat},${it.lon}", index.toString()))
                    }

                    if (locations.isEmpty()) {
                        _uiState.update {
                            WeatherState.Error
                        }
                    }

                    loadWeatherInfoForMultipleCities(Locations(locations = locations))
                }
                is Resource.Error -> {
                    _uiState.update {
                        WeatherState.Error
                    }
                }
            }
        }
    }

    internal fun onCitySelected(weatherSummary: WeatherSummary) {
        _uiState.update {
            WeatherState.Data(weatherSummary = weatherSummary)
        }
        textFieldState.value = TextFieldValue(text = "")
        saveCurrentCity("${weatherSummary.location.lat},${weatherSummary.location.lon}")
    }

    private fun saveCurrentCity(location: String) {
        sharedPreferences.edit().putString(CURRENT_CITY, location).apply()
    }

    private fun getCurrentCity(): String {
        return sharedPreferences.getString(CURRENT_CITY, "").orEmpty()
    }

    companion object {
        private const val CURRENT_CITY = "current_city"
    }
}