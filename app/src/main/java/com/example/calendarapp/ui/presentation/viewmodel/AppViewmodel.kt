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
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.domain.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppViewmodel(application: Application = Application(), utilityHelper: UtilityHelper) : ViewModel(){

    var holidayRepository = HolidayRepository(utilityHelper)
    private val _holidays = MutableLiveData<List<Holiday>>()
    val holidays: LiveData<List<Holiday>> = _holidays

    var allEvents: LiveData<List<Event>>
    var roomRepository: EventRepository
    var searchResults: MutableLiveData<List<Event>>

    init {
        val productDb = EventRoomDatabase.getInstance(application)
        val productDao = productDb.eventDao()
        roomRepository = EventRepository(productDao)

        allEvents = roomRepository.allEvents
        searchResults = roomRepository.searchResults
    }

    //var to determine if a new event gets added or just edited for the edit menu
    var isEditing = false

    //DB FUNCTIONS
    private val eventDb = EventRoomDatabase.getInstance(application)
    private val eventDao = eventDb.eventDao()
    private var dbRepository = EventRepository(eventDao)

    fun insertEvent(event: Event): Boolean {
        return try {
            dbRepository.insertEvent(event)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }

    fun findEventsByName(name: String){
        dbRepository.findEvent(name)
    }

    fun findEventsByDay(day: LocalDateTime) {
        dbRepository.findEventByDay(day)
    }


    fun deleteEvent(name: String): Boolean {
        return try {
            dbRepository.deleteEvent(name)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }

    fun checkConflictingEvents(start: LocalDateTime, end: LocalDateTime): String? {
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

        //Checks if it overlaps with an existing event
        //for now, checks every single event in the array (could be cleaner)
        searchResults.value?.forEach {
            //if the same day...
            if (it.start.dayOfYear != it.start.dayOfYear) {
                //if the end of the it crosses the start time of the event
                if (end.hour * 60 + end.minute >= it.start.hour * 60 + it.start.minute ||
                    start.hour * 60 + start.minute >= it.theEnd.hour * 60 + it.theEnd.minute
                ) {
                    //overlaps either in the top or the bottom! send a message
                    return "Overlaps another event: Check time values"
                }
            }
        }
        //check exact date start & end
        if (start.hour * 60 + start.minute == end.hour * 60 + end.minute) {
            return "Start and End times are the same"
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
}