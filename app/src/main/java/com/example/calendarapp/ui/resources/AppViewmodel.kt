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

    fun checkConflictingEvents(start:LocalDateTime, end: LocalDateTime): String?{
        //Given a start & end, look thru the list of events and find conflicting times & dates
        //returns an error message if a conflict is found, null if not

        //validate if start is before end / equals to each other
        Log.d("what", (start.hour*60 + start.minute).toString() + "-"+ (end.hour*60 + end.minute).toString())
        if(start.hour*60 + start.minute >= end.hour*60 + end.minute){
            return "Start time must be before the end time"
        }

        //Checks if it overlaps with an existing event
        //for now, checks every single event in the array (could be cleaner)
        events.forEach {
            //if the same day...
            if (it.start.dayOfYear != it.start.dayOfYear)
            {
                //if the end of the it crosses the starttime of the event
                if(end.hour*60 + end.minute >= it.start.hour*60 + it.start.minute ||
                    start.hour*60 + start.minute >= it.end.hour*60 + it.end.minute)
                {
                    //overlaps either in the top or the bottom! send a message
                    return "Overlaps another event: Check time values"
                }
            }
        }
        //check exact date start & end
        if(start.hour*60 + start.minute == end.hour*60 + end.minute)
        {
            return "Start and End times are the same"
        }
        return null
    }

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
        //checks conflicting event times

            return try{
                events.add(event)
                true
            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                false
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
