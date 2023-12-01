package com.example.calendarapp.ui.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.data.retrofit.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

class AppViewmodel(application: Application) : ViewModel(){

    val allEvents: LiveData<List<Event>>
    private val eventRepository: EventRepository
    val searchResults: MutableLiveData<List<Event>>
    var currentDay:Date = Date()
    var currentlyViewingEvent:Event? = null
    var isEditing = false

    init {
        val productDb = EventRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        eventRepository = EventRepository(productDao)

        allEvents = eventRepository.allEvents
        searchResults = eventRepository.searchResults
    }


    private val holidayRepository = HolidayRepository()
    private val _holidays = MutableLiveData<List<Holiday>>()
    val holidays: LiveData<List<Holiday>> = _holidays



    fun insertEvent(event: Event): Boolean {
        return try {
            eventRepository.insertEvent(event)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }

    fun findEventByName(name: String) {
        eventRepository.findEventsByName(name)
    }

    fun findEventByDay(day: Date):List<Event>? {
        eventRepository.findEventsByDay(day)
        return allEvents.value
    }


    fun deleteEvent(name: String):Boolean {
        return try {
            eventRepository.deleteEvent(name)
            true
        } catch (e:Exception){
            false
        }
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

    fun getDaysWithEvents(month: YearMonth): List<Date>? {
        val events: List<Event>? = allEvents.value
        return if (events != null) {
            val monthsEvents = events.filter {

                val c: Calendar = Calendar.getInstance()
                c.time = it.day
                val theDay: Int = c.get(Calendar.DAY_OF_WEEK)
                val theMonth: Int = c.get(Calendar.MONTH)
                val eventMonth = YearMonth.of(theDay, theMonth)
                eventMonth == month
            }
            val eventDates = monthsEvents.map { it.start }.toSet()
            eventDates.toList()
        } else {
            null
        }
    }

    fun setNewDay(d:Date){
        currentDay = d
    }

    fun setCurrentEvent(event: Event) {
        currentlyViewingEvent = event
    }

    fun checkConflictingEvents(start: Date, end: Date): String? {
        //Given a start & end, look thru the list of events and find conflicting times & dates
        //returns an error message if a conflict is found, null if not

        //validate if start is before end / equals to each other
        //Log.d("what", (start.hour*60 + start.minute).toString() + "-"+ (end.hour*60 + end.minute).toString())

        val calStart: Calendar = Calendar.getInstance()
        calStart.time = start
        val calEnd: Calendar = Calendar.getInstance()
        calEnd.time = end

        if (calStart.get(Calendar.HOUR_OF_DAY) * 60 + calStart.get(Calendar.MINUTE) >= calEnd.get(
                Calendar.HOUR_OF_DAY
            ) * 60 + calEnd.get(Calendar.MINUTE)
        ) {
            return "Start time must be before the end time"
        }

        //Checks if it overlaps with an existing event
        //for now, checks every single event in the array (could be cleaner)
        allEvents.value?.forEach {
            //if the same day...
            val calStartEv: Calendar = Calendar.getInstance()
            calStartEv.time = it.start
            val calEndEv: Calendar = Calendar.getInstance()
            calEndEv.time = it.theEnd
            if (calStartEv.get(Calendar.DAY_OF_WEEK) != calStartEv.get(Calendar.HOUR_OF_DAY)) {
                //if the end of the it crosses the starttime of the event
                if (calEndEv.get(Calendar.HOUR_OF_DAY) * 60 + calEndEv.get(Calendar.MINUTE) >= calStartEv.get(
                        Calendar.HOUR_OF_DAY
                    ) * 60 + calStartEv.get(Calendar.MINUTE) ||
                    calStartEv.get(Calendar.HOUR_OF_DAY) * 60 + calStartEv.get(Calendar.MINUTE) >= calEndEv.get(
                        Calendar.HOUR_OF_DAY
                    ) * 60 + calEndEv.get(Calendar.MINUTE)
                ) {
                    //overlaps either in the top or the bottom! send a message
                    return "Overlaps another event: Check time values"
                }
            }
        }

        //check exact date start & end
        if(calStart.get(Calendar.HOUR_OF_DAY)*60 + calStart.get(Calendar.MINUTE) == calEnd.get(Calendar.HOUR_OF_DAY)*60 + calEnd.get(Calendar.MINUTE))
        {
            return "Start and End times are the same"
        }
        return null
    }
}