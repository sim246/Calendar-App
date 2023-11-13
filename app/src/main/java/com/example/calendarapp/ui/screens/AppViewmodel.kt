package com.example.calendarapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calendarapp.ui.resources.Event
import java.time.LocalDateTime

class AppViewmodel() : ViewModel(){
    //I ASSUME this is the place where we get the event list from the DB,
    //and then we can fetch them based on it so we can pass them over
    //to the composables to render

    //for milestone 1 at least, we should just make some placeholder data to display



    //the placeholder event to be obtained by the Events composable.
    //In the future, there will be a list of events, but this one describes the one
    //currently being viewed / edited
    @RequiresApi(Build.VERSION_CODES.O)
    var currentViewingEvent = mutableStateOf(Event("Placeholder Event", LocalDateTime.parse("2021-05-18T15:15:00"), LocalDateTime.parse("2021-05-18T15:15:00")))

    //Func to delete the event from the event list
    fun DeleteEvent(event: Event){

    }

    //function to add a new event to the db / event list
    fun AddEvent(event: Event){

    }




}