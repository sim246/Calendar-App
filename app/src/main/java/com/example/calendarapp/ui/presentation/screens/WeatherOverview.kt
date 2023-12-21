package com.example.calendarapp.ui.presentation.screens

import android.util.Log
import android.widget.ScrollView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calendarapp.ui.domain.Weather
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel

//The weather overview screen

@Composable
fun WeatherSingleDay(viewmodel: AppViewmodel){
    Log.d("WeatherUI", "Rendering WeatherSingleDay")
    //The weather for a single day (for the 5 days after the current date)
    //should just display a weather card for the current date being viewed
    //(obtained via viewmodel)
    val weather = viewmodel.getCurrentWeatherFromArray()
    WeatherCard(weather)
}
@Composable
fun WeatherCurrentDay(viewmodel: AppViewmodel){
    Log.d("WeatherUI", "Rendering WeatherCurrentDay")
    //The weather for the current day (the current day's weather + 5 days ahead)
    //I feel this should be the above but x5 in a scrollable view
    val currentDayWeather: Weather? = viewmodel.WeatherDownloader.weatherCurrentDay
    if(currentDayWeather !== null)
    {
        Column (modifier= Modifier.verticalScroll(rememberScrollState())){
            WeatherCard(currentDayWeather)
            for (weather in viewmodel.WeatherDownloader.weatherFiveDays) {
                WeatherCard(weather)
            }
        }
    }

}
@Composable
fun WeatherCard(weather: Weather){
    //The card object itself, in a vertical style.
    Column(Modifier.fillMaxWidth()){
        Text("Weather for " + weather.day)
        Text(weather.temperature.toString() + " degrees C")
        Text("Feels Like" + weather.temperatureFeelsLike.toString() + " degrees C")
        Text(weather.condition)
        Text("Humidity: " + weather.humidity)
        Divider(color = Color.Gray, modifier = Modifier
            .fillMaxWidth()
            .width(1.dp))
    }
}

