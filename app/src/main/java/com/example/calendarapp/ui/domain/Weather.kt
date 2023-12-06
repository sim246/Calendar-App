package com.example.calendarapp.ui.domain

//GENERAL WEATHER MENU REQUIREMENTS:::::
/*
display the 5-day forecast with 3-hour step, for the location. Your app should update the
 weather information at 10-minute intervals, and it should display the date and time the
 weather information was last updated
 */

data class Weather(
//should describe the weather for a day.

    val day:String = "",
    val condition:String = "",
    val temperature: Double = 0.0,
    val temperatureFeelsLike: Double = 0.0,
    val humidity:Double = 0.0,
    val UVIndex: Double= 0.0,
)
