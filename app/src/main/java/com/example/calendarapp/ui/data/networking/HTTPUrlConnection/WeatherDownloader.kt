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
import java.time.LocalDateTime


class WeatherDownloader(fusedLocationClient: FusedLocationProviderClient, context: Context) {
    private var currentLocation:Location? = null

    private val APIKEY : String = "ec5cfdc73b7f456e8232bd9c29394e68"

    //currentWeather for the fetched day
    //var currentWeather: Weather? = null

    var lastUpdated: LocalDateTime = LocalDateTime.now()


    var weatherCurrentDay: Weather? = null
    var weather3HRStep = mutableListOf<Weather>()
    var weatherFiveDays = mutableListOf<Weather>()

    init {
       try{
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
       catch(e: Exception){
           Log.e("WeatherDownloader", e.stackTraceToString())
       }

    }

    //Ran at the start of the app, should run again if the current date changes
    fun loadWeather(){
       try{
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
               weather.day = LocalDate.now().toString()
               weather.temperature = jObject.getJSONObject("main").getString("temp").toDouble()
               weather.temperatureFeelsLike = jObject.getJSONObject("main").getString("feels_like").toDouble()
               weather.humidity = jObject.getJSONObject("main").getString("humidity").toDouble()
               //add to list
               weatherList.plus(weather)

               weatherCurrentDay = weather


           } else {
               Log.e("WeatherDownloader", "REST FETCH ERROR: $responseCode $httpURLConnection.responseMessage");
               weatherCurrentDay = null

           }

           //------------------------------------------------------------------------------

           //SET 5 DAYS WEATHER

           //Loads JSON into the weather livedata.

           //init the list of dates to obtain
           val datesToFetch = mutableListOf<String>()
           var currentWeatherDay = LocalDate.now()
           Log.d("WeatherDownloader", LocalDate.now().toString())
           for(i in 1..5){
               currentWeatherDay = currentWeatherDay.plusDays(1)
               datesToFetch.add(currentWeatherDay.toString())
           }
           //Values from the above list will be used to get 1 weather entry per the following days after today


           Log.d("WeatherDownloader", "Runnng Load Multipledayforecast")
           url = URL("https://api.openweathermap.org/data/2.5/forecast?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&cnt=80&appid=${APIKEY}")
           //url = URL("https://api.openweathermap.org/data/2.5/forecast/daily?lat=${currentLocation!!.latitude}&lon=${currentLocation!!.longitude}&cnt=5&appid=${APIKEY}")
           // the onecall 3.0 doesn't work, you need a sub for it??
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
               val todaysDate = LocalDate.now().toString()
               for (i in 0 until weatherArray.length()){
                   val weatherJSON : JSONObject = weatherArray.getJSONObject(i)
                   //if current date hasn't been added yet (timetext starts with any in the list)
                   val weatherDay = weatherJSON.getString("dt_txt").split(" ")[0]
                   if(datesToFetch.any { weatherDay.contains(it, ignoreCase = true) })
                   {
                       val weather = getObjectFromJSON(weatherJSON, weatherDay)
                       //add to list
                       weatherFiveDays.add(weather)
                       datesToFetch.remove(weatherDay)
                   }
                   else if(weatherDay == todaysDate){
                       //if today, add to 3hr step list
                       val weather = getObjectFromJSON(weatherJSON, weatherDay)
                       weather3HRStep.add(weather)
                   }
               }
           } else {
               Log.e("WeatherDownloader", "REST FETCH ERROR: $responseCode $httpURLConnection.responseMessage");
               weatherFiveDays.clear()
           }
       }
       catch(e: Exception){
            Log.e("WeatherDownloader", e.stackTraceToString())
           //reset vars
           weatherCurrentDay = null
           weatherFiveDays = mutableListOf<Weather>()
       }
        lastUpdated = LocalDateTime.now()
    }
}


fun getObjectFromJSON(weatherJSON:JSONObject, weatherDay:String):Weather{
    val weather = Weather()
    //set data
    weather.condition = weatherJSON.getJSONArray("weather").getJSONObject(0).getString("main")
    weather.day = weatherDay
    weather.temperature = weatherJSON.getJSONObject("main").getString("temp").toDouble()
    weather.temperatureFeelsLike = weatherJSON.getJSONObject("main").getString("feels_like").toDouble()
    weather.humidity = weatherJSON.getJSONObject("main").getString("humidity").toDouble()
    return weather
}