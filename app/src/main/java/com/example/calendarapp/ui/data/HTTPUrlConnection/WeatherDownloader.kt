package com.example.calendarapp.ui.data.HTTPUrlConnection


import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import com.example.calendarapp.ui.domain.Weather
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.google.android.gms.location.FusedLocationProviderClient
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class WeatherDownloader(application: Application, viewmodel: AppViewmodel) {

    private var viewmodel : AppViewmodel = viewmodel
    private var currentLocation:Location? = null
    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private val APIKEY : String = "ec5cfdc73b7f456e8232bd9c29394e68"


    //Function that gets a weather object, returns null if not findable (no location perms for example)
    fun fetchData(context: Context, fusedLocationClient: FusedLocationProviderClient){
        //should check in cache if the data exists already
        //please pass in the current datetime of the fetch so we can get specific dates!!!
        //below should be gotten by device location somehow
//        updateLocation(fusedLocationClient)
        loadJSON()


    }
    //Updates currentLocation
    private fun updateLocation(fusedLocationClient: FusedLocationProviderClient){
        Log.d("WeatherDownloader", "Attempting to fetch location")
        try{
            Log.d("WeatherDownloader", "Current location updated! Doing JSON fetch.")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    //Sets currentlocation global var to the gotten location.
                    currentLocation = location
                    Log.d("WeatherDownloader", "Current location updated! Doing JSON fetch.")
                    loadJSON()
                }
        } catch(e: SecurityException){
            Log.e("WeatherDownloader", "Security Error when obtaining location: $e")

        }
    }


    fun loadJSON(): Weather?{
        Log.d("WeatherDownloader", "Runnng LoadJSON")
        //Uncomment the below line ONLY IF the above fn is working (will always be null and never fetch JSON otherwise)
        //if(currentLocation === null){
        //    Log.d("WeatherDownloader", "location is null")
        //    return null
        //}
        Log.d("WeatherDownloader", "location isn;t null")

        //val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&appid=${APIKEY}")
        val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=1&lon=1&appid=${APIKEY}")
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        val responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }
            //return weather with JSON translated data
            Log.d("WeatherDownloader", dataString)

            var weather = Weather()
            val jObject = JSONObject(dataString)
            weather.condition = jObject.getJSONArray("weather").getJSONObject(1).getString("main")
            weather.day = "Today (temp value)"
            weather.temperature = jObject.getJSONObject("main").getString("temp").toDouble()
            weather.temperatureFeelsLike = jObject.getJSONObject("main").getString("feels_like").toDouble()
            weather.UVIndex =

            return weather

        } else {
            Log.e("WeatherDownloader", "REST FETCH ERROR: $responseCode")
            return null
        }
    }

    //Below FN translates JSON string into usable data
    fun parseJSON(){

    }
}
