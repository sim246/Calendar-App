package com.example.calendarapp.ui.resources

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calendarapp.ui.resources.Event
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class AppViewmodel() : ViewModel(){
    //I ASSUME this is the place where we get the event list from the DB,
    //and then we can fetch them based on it so we can pass them over
    //to the composables to render

    //for milestone 1 at least, we should just make some placeholder data to display



    //the placeholder event to be obtained by the Events composable.
    //In the future, there will be a list of events, but this one describes the one
    //currently being viewed / edited.
    //Should be edited by functions in this viewmodel.
    var currentlyViewingEvent = Event("Placeholder Event", LocalDateTime.parse("2021-05-18T15:15:00"), LocalDateTime.parse("2021-05-18T15:15:00"))

    val events: MutableList<Event> = mutableListOf(
        Event(
            "event",
            LocalDateTime.parse("2023-11-18T13:15:00"),
            LocalDateTime.parse("2023-11-13T16:30:00"),
            "des",
            "name",
            "loc"
        ),
        Event(
            "event",
            LocalDateTime.parse("2021-05-18T08:15:00"),
            LocalDateTime.parse("2021-05-18T10:30:00"),
            "des",
            "name",
            "loc"
        )
    )

    var day:LocalDate by mutableStateOf(LocalDate.parse("2023-11-18"))

    fun ReplaceDay(d:LocalDate){
        day = d
    }

    //Func to delete the event from the event list
    fun DeleteEvent(event: Event): Boolean{
        //Should return a bool if it was successful or not.
        try {
            events.remove(event)
            return true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            return false
        }
    }

    //function to add a new event to the db / event list
    fun AddEvent(event: Event): Boolean{
        //Should return a bool if it was successful or not.
        try{
            events.add(event)
            return true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            return false
        }
    }




}