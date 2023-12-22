package com.example.calendarapp.ui.presentation.screens

import android.util.Log
import android.widget.ScrollView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendarapp.R
import com.example.calendarapp.ui.domain.Weather
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel

//The weather overview screen

@Composable
fun WeatherSingleDay(viewmodel: AppViewmodel, navController: NavController){
    Log.d("WeatherUI", "Rendering WeatherSingleDay")
    //The weather for a single day (for the 5 days after the current date)
    //should just display a weather card for the current date being viewed
    //(obtained via viewmodel)
    val weather = viewmodel.getCurrentWeatherFromArray()
    Column {
        Button(onClick = {navController.popBackStack()}) {
            Text(stringResource(R.string.return_btn))
        }
        WeatherCard(weather)
    }

}
@Composable
fun WeatherCurrentDay(viewmodel: AppViewmodel, navController: NavController){
    Log.d("WeatherUI", "Rendering WeatherCurrentDay")
    //The weather for the current day (the current day's weather + 5 days ahead)
    //I feel this should be the above but x5 in a scrollable view
    Column{
        Button(onClick = {navController.popBackStack()}) {
            Text(stringResource(R.string.return_btn))
        }
        val currentDayWeather: Weather? = viewmodel.WeatherDownloader.weatherCurrentDay
        if(currentDayWeather !== null)
        {
            Column (modifier= Modifier.verticalScroll(rememberScrollState())){
                Text(stringResource(R.string.weather_todaytitle), fontWeight = FontWeight.Bold)

                WeatherCard(currentDayWeather)

                val threeHourStep = viewmodel.WeatherDownloader.weather3HRStep

                if(threeHourStep.isNotEmpty())
                {
                    Text(stringResource(R.string.weather_3hrsteptitle), fontWeight = FontWeight.Bold)
                    for (weather in viewmodel.WeatherDownloader.weather3HRStep) {
                        WeatherCard(weather)
                    }
                }
                Text(stringResource(R.string.weather_5daystitle), fontWeight = FontWeight.Bold)

                for (weather in viewmodel.WeatherDownloader.weatherFiveDays) {
                    WeatherCard(weather)
                }
            }
        }
    }
}
@Composable
fun WeatherCard(weather: Weather){
    //The card object itself, in a vertical style.
    Column(Modifier.fillMaxWidth()){
        Text(stringResource(R.string.weather_for) + weather.day)
        Text(weather.temperature.toString() + stringResource(R.string.weather_degrees))
        Text(stringResource(R.string.weather_feelslike) + weather.temperatureFeelsLike.toString() + stringResource(R.string.weather_degrees))
        Text(stringResource(R.string.weather_condition) + weather.condition)
        Text(stringResource(R.string.weather_humidity) + weather.humidity)
        CardDivider()
    }
}


@Composable
fun CardDivider(){
    Divider(color = Color.Gray, modifier = Modifier
        .fillMaxWidth()
        .width(1.dp))
}
