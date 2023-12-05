package com.example.calendarapp.ui.data.HTTPUrlConnection


import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import com.example.calendarapp.ui.domain.UtilityHelper
import com.example.calendarapp.ui.domain.Weather

class WeatherDownloader(utilityHelper: UtilityHelper) {
    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private val APIKEY : String = "ec5cfdc73b7f456e8232bd9c29394e68"
    val utilHelper = utilityHelper

    //Function that gets a weather object, returns null if not findable (no location perms for example)
    fun fetchData(tempfile: String) : Weather? {
        //should check in cache if the data exists already

        //below should be gotten by device location somehow
        val latitude = 1
        val longitude = 1

        val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=${APIKEY}")
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
            return Weather()

        } else {
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
            return null
        }
    }
}


