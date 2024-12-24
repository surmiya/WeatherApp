package com.nooro.weatherapp.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import com.nooro.weatherapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nooro.weatherapp.domain.model.WeatherSummary

@Composable
fun WeatherAppContent(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadWeatherInfo()
    }

    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(viewModel)
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchResults(viewModel)
        }
    }
}

@Composable
fun SearchBar(viewModel: WeatherViewModel) {
    val textFieldState by remember {
        viewModel.textFieldState
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        value = textFieldState.text,
        onValueChange = {
            viewModel.textFieldState.value = TextFieldValue(it)
        },
        singleLine = true,
        placeholder = {
            Text(
                text = "Search Location",
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable(enabled = true, role = Role.Button) {
                    viewModel.searchCities(textFieldState.text)
                    keyboardController?.hide()
                },
                imageVector = Icons.Outlined.Search,
                tint = Color.DarkGray,
                contentDescription = "Search"
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                viewModel.searchCities(textFieldState.text)
            }
        )
    )
}

@Composable
private fun SearchResultCard(weatherData: WeatherSummary, viewModel: WeatherViewModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    viewModel.onCitySelected(weatherData)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = weatherData.location.name,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = weatherData.location.country,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${weatherData.current.temp_c.toInt()}°",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${weatherData.current.condition.icon}")
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather Condition",
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(84.dp)
                    .weight(weight = 1f),
            )
        }
    }
}

@Composable
fun CurrentWeatherContent(weatherSummary: WeatherSummary) {
    val currentWeather = weatherSummary.current
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https:${currentWeather.condition.icon}")
            .crossfade(true)
            .build(),
        contentDescription = "Weather Condition",
        modifier = Modifier.size(148.dp)
    )
    Row {
        Text(
            text = weatherSummary.location.name,
            fontSize = 38.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.location_icon),
            tint = Color.Black,
            contentDescription = "",
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "${currentWeather.temp_c.toInt()}°",
        fontSize = 60.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherDetailColumn("Humidity", " ${currentWeather.humidity}%")
            WeatherDetailColumn("UV", "${currentWeather.uv.toInt()}")
            WeatherDetailColumn("Feels Like", "${currentWeather.feelslike_c}°")
        }
    }
}

@Composable
fun WeatherDetailColumn(title: String, value: String) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize =12.sp
        )
    }
}

@Composable
fun EmptyContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No City Selected",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Please search for a City",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize =16.sp
        )
    }
}

@Composable
private fun SearchResults(viewModel: WeatherViewModel) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is WeatherState.Error -> {
            Text(
                text = "No Matching cities, Please try again!",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }

        is WeatherState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is WeatherState.Data -> {
            uiState.weatherSummary?.let {
                CurrentWeatherContent(weatherSummary = it)
            } ?: run {
                uiState.weatherInfoPerCity?.bulk?.forEach { bulkInfo ->
                    Spacer(modifier = Modifier.height(8.dp))
                    SearchResultCard(bulkInfo.query, viewModel)
                }
            }
        }

        WeatherState.Empty -> {
            EmptyContent()
        }
    }
}