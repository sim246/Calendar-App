package com.example.calendarapp.ui.domain

//GENERAL WEATHER MENU REQUIREMENTS:::::
/*
display the 5-day forecast with 3-hour step, for the location. Your app should update the
 weather information at 10-minute intervals, and it should display the date and time the
 weather information was last updated
 */

class Weather(
//should describe the weather for a day.

    var day:String = "",
    var condition:String = "",
    var temperature: Double = 0.0,
    var temperatureFeelsLike: Double = 0.0,
    var humidity:Double = 0.0,
    var UVIndex: Double= 0.0,
)
