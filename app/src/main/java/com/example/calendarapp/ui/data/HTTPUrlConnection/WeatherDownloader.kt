package com.example.calendarapp.ui.data.HTTPUrlConnection


import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import com.example.calendarapp.ui.domain.UtilityHelper
import com.example.calendarapp.ui.domain.Weather

class WeatherDownloader(utilityHelper: UtilityHelper) {
    private val theUrl = "https://date.nager.at/api/v3/NextPublicHolidays/CA"
    val utilHelper = utilityHelper

    fun fetchData(tempfile: String) : Weather? {
        val url = URL(theUrl)
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


