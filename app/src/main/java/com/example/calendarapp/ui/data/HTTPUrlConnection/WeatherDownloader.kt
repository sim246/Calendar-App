package com.example.calendarapp.ui.data.HTTPUrlConnection


import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import java.net.HttpURLConnection
import java.net.URL
import com.example.calendarapp.ui.domain.UtilityHelper
import com.example.calendarapp.ui.domain.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class WeatherDownloader(application: Application) {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private var currentLocation:Location? = null
    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private val APIKEY : String = "ec5cfdc73b7f456e8232bd9c29394e68"


    //Function that gets a weather object, returns null if not findable (no location perms for example)
    fun fetchData(context: Context) : Weather? {
        //should check in cache if the data exists already

        //below should be gotten by device location somehow
        updateLocation()
        if(currentLocation === null){return null}

        val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&appid=${APIKEY}")
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        val responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }

            //Write the data string to the temp file
            //TempStorage(utilHelper).writeDataToFile(dataString, tempfile)
            return Weather("today")

        } else {
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
            return null
        }
    }
    //Updates currentLocation
    private fun updateLocation(){

        Log.d("WeatherDownloader", "Attempting to fetch location")
         try{
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    currentLocation = location
                    Log.d("WeatherDownloader", "Current location updated!")
                }

        } catch(e: SecurityException){
            Log.e("WeatherDownloader", "Security Error when obtaining location: $e")

        }


    }
}


