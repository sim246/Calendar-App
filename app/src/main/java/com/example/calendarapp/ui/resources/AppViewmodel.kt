package com.example.calendarapp.ui.resources

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class AppViewmodel : ViewModel(){

    var currentlyViewingEvent:Event by mutableStateOf(Event(LocalDate.parse("2023-11-18"),"Placeholder Event", LocalDateTime.parse("2023-11-18T15:15:00"), LocalDateTime.parse("2023-11-18T15:15:00")))

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
            "location"
        ),
        Event(
            LocalDate.parse("2023-11-18"),
            "event 3",
            LocalDateTime.parse("2023-11-18T06:15:00"),
            LocalDateTime.parse("2023-11-18T07:30:00"),
            "description",
            "client name",
            "location"
        )
    )

    var currentDay: LocalDate by mutableStateOf(LocalDate.parse("2023-11-18"))

    fun setNewDay(d:LocalDate){
        currentDay = d
    }

    fun setCurrentEvent(event:Event) {
        currentlyViewingEvent = event
    }

    //Func to delete the event from the event list
    fun deleteEvent(event:Event): Boolean {
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
    fun addEvent(event:Event): Boolean {
        //Should return a bool if it was successful or not.
        return try{
            events.add(event)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }

    fun getDaysWithEvents(month: YearMonth): List<LocalDate> {
        val monthsEvents = events.filter{
            val eventMonth = YearMonth.from(it.start)
            eventMonth == month
        }
        val eventDates = monthsEvents.map { it.start.toLocalDate() }.toSet()

        return eventDates.toList()
    }
}
