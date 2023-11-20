package com.example.calendarapp.ui.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.data.retrofit.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class AppViewmodel : ViewModel(){

    private val repository = HolidayRepository()
    private val _holidays = MutableLiveData<List<Holiday>>()
    val holidays: LiveData<List<Holiday>> = _holidays

    var currentlyViewingEvent: Event by mutableStateOf(Event(LocalDate.parse("2023-11-18"),"Placeholder Event", LocalDateTime.parse("2023-11-18T15:15:00"), LocalDateTime.parse("2023-11-18T15:15:00")))
    //var to determine if a new event gets added or just edited for the edit menu
    var isEditing = false
    val events: MutableList<Event> = mutableListOf(
        Event(
            LocalDate.parse("2023-11-18"),
            "event 1",
            LocalDateTime.parse("2023-11-18T13:15:00"),
            LocalDateTime.parse("2023-11-18T16:30:00"),
            "description",
            "client name",
            "location"
        ),
        Event(
            LocalDate.parse("2023-11-18"),
            "event 2",
            LocalDateTime.parse("2023-11-18T08:15:00"),
            LocalDateTime.parse("2023-11-18T10:30:00"),
            "description",
            "client name",
            "Central Park"
        ),
        Event(
            LocalDate.parse("2023-11-18"),
            "event 3",
            LocalDateTime.parse("2023-11-18T06:15:00"),
            LocalDateTime.parse("2023-11-18T07:30:00"),
            "description",
            "client name",
            "DisneyLand"
        )
    )

    fun checkConflictingEvents(start: LocalDateTime, end: LocalDateTime): Boolean{
        //Given a start & end, look thru the list of events and find conflicting times & dates
        //returns true if a conflict is found, false if not
        return false
    }

    var currentDay: LocalDate by mutableStateOf(LocalDate.parse("2023-11-18"))

    fun setNewDay(d:LocalDate){
        currentDay = d
    }

    fun setCurrentEvent(event: Event) {
        currentlyViewingEvent = event
    }

    //Func to delete the event from the event list
    fun deleteEvent(event: Event): Boolean {
        //Should return a bool if it was successful or not.
        return try {
            events.remove(event)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }

    //function to add a new event to the db / event list
    fun addEvent(event: Event): Boolean {
        //Should return a bool if it was successful or not.
        //checks conflicting event times
        if(!checkConflictingEvents(event.start, event.end)){
            return try{
                events.add(event)
                true
            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                false
            }
        }
        return false


    }

    fun fetchHolidays() {
        viewModelScope.launch {
            try {
                val hol = repository.getHolidays()
                _holidays.value = hol
                Log.e("FetchHoliday", _holidays.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("FetchHoliday", e.message.toString())
            }
        }
    }

    //get all days with events
    fun getDaysWithEvents(month: YearMonth): List<LocalDate> {
        val monthsEvents = events.filter{
            val eventMonth = YearMonth.from(it.start)
            eventMonth == month
        }
        val eventDates = monthsEvents.map { it.start.toLocalDate() }.toSet()

        return eventDates.toList()
    }

    //get all the events for a date
    fun getEventsForDay(date: LocalDate): List<Event> {
        return events.filter { event ->
            val eventDate = event.start.toLocalDate()
            eventDate == date
        }
    }
}