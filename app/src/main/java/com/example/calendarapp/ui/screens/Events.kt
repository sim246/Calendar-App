package com.example.calendarapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendarapp.ui.resources.Event
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateEventMenu(event: Event = Event("", LocalDateTime.parse("2021-05-18T15:15:00"),
    LocalDateTime.parse("2021-05-18T15:16:00"),"","")){

        /*
        Required:
        Title + Description
        Location
        Time
        Day (auto-specified by adding from day view or user-specified)
         ??? (probably nothing else)
        */
        Column(){



            var titleString = EventInputField("Title")
            var descriptionString = EventInputField("Description")
            var locationString = EventInputField("Location")
            var clientString = EventInputField("Client")
            EventTimeDisplay(event)
            Button(
                content={Text(text = "Save Event")},
                //should save the event at the specified date and time onclick
                onClick={
                    //isEditing = false
                    //saves the event somehow with the specified params
                }
            )
            Button(
                content={Text(text = "Quit without saving")},
                onClick={
                    //isEditing = false
                }
            )

        }




    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventDisplay(event: Event){
        Column(){


            Text(text=event.eventName)
            Text(text="@ " + event.location)
            Text(text=event.start.toString() + " to " + event.end.toString())
            Text(text=event.description)
            Button(
                content={Text(text = "Edit Event")},
                onClick={}
            )
            Button(
                content={Text(text = "Delete Event")},
                onClick={}
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    @ExperimentalMaterial3Api
    fun EventTimeDisplay(event: Event){
        var showTimePicker by remember { mutableStateOf(false) }
        //val time = event.start.hour.toString() + ":"+ event.start.minute +":"+ event.start.second

        //Text("Time: $time")


        if (showTimePicker) {

            Button(onClick={showTimePicker = false}, content={Text(text = "Save")})

        }
        else
        {
            Button(onClick={}, content={Text(text = "Set Start Time")})
            Button(onClick={}, content={Text(text = "Set End Time")})
        }


    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventInputField(name:String): String {
        var inputText by rememberSaveable {mutableStateOf("")}
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(name) }
        )
        return inputText
    }



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
    @Composable
    fun CreateEventMenuPreview(){
        val ev = Event("title",LocalDateTime.parse("2021-05-18T15:15:00"),
            LocalDateTime.parse("2021-05-18T15:16:00"),"desc","client","location")
        CreateEventMenu(ev)
    }


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
    @Composable
    fun EventDisplayPreview(){
    val ev = Event("title",LocalDateTime.parse("2021-05-18T15:15:00"),LocalDateTime.parse("2021-05-18T15:16:00"),"desc","client","location")
        EventDisplay(ev)
    }



