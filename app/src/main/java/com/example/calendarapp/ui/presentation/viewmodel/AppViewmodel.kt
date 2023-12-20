package com.example.calendarapp.ui.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.ui.data.HTTPUrlConnection.WeatherDownloader
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.domain.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import com.example.calendarapp.ui.domain.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppViewmodel(application: Application = Application(), utilityHelper: UtilityHelper,fusedLocationProvider: FusedLocationProviderClient) : ViewModel(){

    private val fusedLocationProviderClient = fusedLocationProvider
    val utilityHelper = utilityHelper

    //Location Context
    var WeatherDownloader: WeatherDownloader = WeatherDownloader()


    var holidayRepository = HolidayRepository(utilityHelper)
    private val _holidays = MutableLiveData<List<Holiday>>()
    val holidays: LiveData<List<Holiday>> = _holidays

    var allEvents: LiveData<List<Event>>
    var roomRepository: EventRepository
    var searchResults: MutableLiveData<List<Event>> = MutableLiveData()

    init {
        val eventDb = EventRoomDatabase.getInstance(application)
        val eventDao = eventDb.eventDao()
        roomRepository = EventRepository(eventDao)
        allEvents = roomRepository.getAllEvents()
    }

    fun insertEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.insertEvent(event)
        }
    }

    fun findEventsByDay(day: LocalDateTime) {
        viewModelScope.launch (Dispatchers.IO){
            searchResults.postValue(roomRepository.findEventByDay(day))
        }
    }

    fun deleteEvent(id: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            roomRepository.deleteEvent(id)
        }
    }

    fun checkConflictingEvents(id:Int, start: LocalDateTime, end: LocalDateTime, allEvents:List<Event>): String? {
        //Given a start & end, look thru the list of events and find conflicting times & dates
        //returns an error message if a conflict is found, null if not
        //validate if start is before end / equals to each other
        Log.d(
            "what",
            (start.hour * 60 + start.minute).toString() + "-" + (end.hour * 60 + end.minute).toString()
        )
        if (start.hour * 60 + start.minute >= end.hour * 60 + end.minute) {
            return "Start time must be before the end time"
        }
        //validate if start and end are between 6 am and 12 pm
        if (start.hour !in 6..24 || end.hour !in 6..24){
            return "events must be between 6 am and 12 pm"
        }
        //check exact date start & end
        if (start.hour * 60 + start.minute == end.hour * 60 + end.minute) {
            return "Start and End times are the same"
        }

        //Checks if it overlaps with an existing event
        //for now, checks every single event in the array (could be cleaner)
        allEvents.forEach {
            //if the same day...
            if (it.start.toLocalDate() == start.toLocalDate() && id != it.id) {
                Log.d("day", (start.hour * 60 + start.minute).toString())
                Log.d("day range " + it.eventName, (it.start.hour * 60 + it.start.minute).toString() + " " + (it.theEnd.hour * 60 + it.theEnd.minute).toString())
                if ((start.hour * 60 + start.minute) in (it.start.hour * 60 + it.start.minute)..(it.theEnd.hour * 60 + it.theEnd.minute)) {
                    //overlaps start time! send a message
                    return "Start time overlaps another event, check time values"
                }

                if ((end.hour * 60 + end.minute) in (it.start.hour * 60 + it.start.minute)..(it.theEnd.hour * 60 + it.theEnd.minute)) {
                    //overlaps end time! send a message
                    return "End time overlaps another event, check time values"
                }
            }
        }
        return null
    }

    var currentDay: LocalDateTime by mutableStateOf(LocalDateTime.now())
    fun setNewDay(d:LocalDateTime){
        currentDay = d
    }

    var currentlyViewingEvent:Event? = allEvents.value?.get(0)
    fun setCurrentEvent(event: Event) {
        currentlyViewingEvent = event
    }

    fun fetchHolidays() {
        viewModelScope.launch {
            try {
                val hol = holidayRepository.getHolidays()
                _holidays.value = hol
                Log.e("FetchHoliday", _holidays.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("FetchHoliday", e.message.toString())
            }
        }
    }

    fun getCurrentDayForecast(utilityHelper: UtilityHelper){
        viewModelScope.launch (Dispatchers.IO){
            //below should set the viewmodel's livedata to the fetched weather data
            WeatherDownloader.fetchData(utilityHelper.context,fusedLocationProviderClient, viewModelScope)
        }
    }
}