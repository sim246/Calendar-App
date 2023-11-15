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

@RequiresApi(Build.VERSION_CODES.O)
class AppViewmodel() : ViewModel(){

    var currentlyViewingEvent:Event by mutableStateOf(Event("Placeholder Event", LocalDateTime.parse("2021-05-18T15:15:00"), LocalDateTime.parse("2021-05-18T15:15:00")))

    val events: MutableList<Event> = mutableListOf(
        Event(
            "event 1",
            LocalDateTime.parse("2023-11-18T13:15:00"),
            LocalDateTime.parse("2023-11-13T16:30:00"),
            "description",
            "client name",
            "location"
        ),
        Event(
            "event 2",
            LocalDateTime.parse("2021-05-18T08:15:00"),
            LocalDateTime.parse("2021-05-18T10:30:00"),
            "description",
            "client name",
            "location"
        ),
        Event(
            "event 3",
            LocalDateTime.parse("2021-05-18T06:15:00"),
            LocalDateTime.parse("2021-05-18T07:30:00"),
            "description",
            "client name",
            "location"
        )
    )

    var day:LocalDate by mutableStateOf(LocalDate.parse("2023-11-18"))

//    fun setDay(d:LocalDate){
//        day = d
//    }

    fun SetCurrentlyViewingEvent(event:Event) {
        currentlyViewingEvent = event
    }

    //Func to delete the event from the event list
    fun DeleteEvent(event:Event): Boolean {
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
    fun AddEvent(event:Event): Boolean {
        //Should return a bool if it was successful or not.
        return try{
            events.add(event)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }
}