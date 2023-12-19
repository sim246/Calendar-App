package com.example.calendarapp.ui.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.calendarapp.ui.domain.Weather

//The weather overview screen

@Composable
fun WeatherSingle(weather: Weather){
    //The weather for a single day (for the 5 days after the current date)
    //should just display a weather card for the current date
    Column(){
        Text("Weather for " + weather.day)
        Text(weather.temperature.toString() + " degrees C")
        Text("Feels Like" + weather.temperatureFeelsLike.toString() + " degrees C")
        Text(weather.condition)
        Text("Humidity: " + weather.humidity)
        Text("UV Index: " + weather.UVIndex)
    }
}
@Composable
fun WeatherMultiple(weatherList : List<Weather>){
    //The weather for the current day (the current day's weather + 5 days ahead)
    //I feel this should be the above but x5 in a scrollable view

}