package com.example.calendarapp.ui.data.networking.HTTPUrlConnection


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import com.example.calendarapp.ui.domain.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import org.json.JSONArray
import java.time.LocalDate


class WeatherDownloader(fusedLocationClient: FusedLocationProviderClient, context: Context) {
    private var currentLocation:Location? = null
    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private val APIKEY : String = "ec5cfdc73b7f456e8232bd9c29394e68"

    //currentWeather for the fetched day
    //var currentWeather: Weather? = null

    var weatherCurrentDay: Weather? = null
    var weatherFiveDays = mutableListOf<Weather>()

    init {
        //Get location on class init (location will likely not change drastically on each app boot)
        Log.d("WeatherDownloader", "Attempting to fetch location")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            val requestCode = 123
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestCode
            )
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                currentLocation = location
                Log.d("WeatherDownloader", location.toString())
            }

    }

    //Ran at the start of the app, should run again if the current date changes
    fun loadWeatherToday(){
        //the one for multiple days only starts on the next day so we need to do this too
        //Loads JSON into the weather livedata.
        Log.d("WeatherDownloader", "Runnng LoadJSON")
        //Uncomment the below line ONLY IF the above fn is working (will always be null and never fetch JSON otherwise)
        if(currentLocation === null){
            Log.d("WeatherDownloader", "location is null")
            weatherCurrentDay = null
            return
        }
        Log.d("WeatherDownloader", "location isn;t null")


        //val url = URL("https://api.openweathermap.org/data/2.5/forecast?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&appid=${APIKEY}")
        var url = URL("https://api.openweathermap.org/data/2.5/weather?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&appid=${APIKEY}")
        var httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        var responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }
            //return weather with JSON translated data
            Log.d("WeatherDownloader", dataString)

            val weatherList = emptyList<Weather>()


            val jObject = JSONObject(dataString)
                val weather = Weather()
                //set data
                weather.condition = jObject.getJSONArray("weather").getJSONObject(0).getString("main")
                //weather.day = "Today (temp value)"
                weather.temperature = jObject.getJSONObject("main").getString("temp").toDouble()
                weather.temperatureFeelsLike = jObject.getJSONObject("main").getString("feels_like").toDouble()

                //add to list
                weatherList.plus(weather)

            weatherCurrentDay = weather


        } else {
            Log.e("WeatherDownloader", "REST FETCH ERROR: $responseCode")
            weatherCurrentDay = null

        }

        //------------------------------------------------------------------------------

        //SET 5 DAYS WEATHER

        //Loads JSON into the weather livedata.
        Log.d("WeatherDownloader", "Runnng LoadJSON")
        //Uncomment the below line ONLY IF the above fn is working (will always be null and never fetch JSON otherwise)
        if(currentLocation === null){
            Log.d("WeatherDownloader", "location is null")
            weatherFiveDays =
            return
        }
        Log.d("WeatherDownloader", "location isn;t null")


        url = URL("https://api.openweathermap.org/data/2.5/forecast?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&appid=${APIKEY}")
        //val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&appid=${APIKEY}")
        httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check if the connection is successful
        responseCode = httpURLConnection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dataString = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }
            //return weather with JSON translated data
            Log.d("WeatherDownloader", dataString)

            weatherFiveDays.clear()


            val jObject = JSONObject(dataString)
            val weatherArray: JSONArray = jObject.getJSONArray("list") // contains a list of all the JSON weathers

            //add 5 weathers to the list
            for (i in 0..4){
                val weather = Weather()
                val weatherJSON : JSONObject = weatherArray.getJSONObject(i)

                //set data
                weather.condition = weatherJSON.getJSONArray("weather").getJSONObject(0).getString("main")
                //weather.day = "Today (temp value)"
                weather.temperature = weatherJSON.getJSONObject("main").getString("temp").toDouble()
                weather.temperatureFeelsLike = weatherJSON.getJSONObject("main").getString("feels_like").toDouble()

                //add to list
                weatherFiveDays.add(weather)
            }



        } else {
            Log.e("WeatherDownloader", "REST FETCH ERROR: $responseCode")
            weatherFiveDays.clear()

        }


    }

    fun loadWeatherFive(){



    }
}
