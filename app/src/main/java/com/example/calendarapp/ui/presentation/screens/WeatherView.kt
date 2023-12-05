package com.example.calendarapp.ui.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendarapp.ui.domain.Weather
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
@Composable
fun WeatherView(weather: Weather){
    Row(){
        Text(text="The weather for [Insert Date Here]")
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherViewPreview(){
    val weather = Weather()
    WeatherView(weather = weather)
}