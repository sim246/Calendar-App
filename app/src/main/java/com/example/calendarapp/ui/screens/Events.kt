package com.example.calendarapp.ui.screens

import android.app.TimePickerDialog
import android.widget.TimePicker
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
import com.example.calendarapp.Event


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateEventMenu(){

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
            EventTimeDisplay()
            Button(
                content={Text(text = "Save Event")},
                //should save the event at the specified date and time onclick
                onClick={
                    //saves the event somehow with the specified params
                }
            )
            Button(
                content={Text(text = "Quit without saving")},
                onClick={}
            )

        }




    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventDisplay(event: Event){
        Column(){


            Text(text=event.title)
            Text(text="@ " + event.location)
            Text(text=event.date + " - " + event.time)
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

    @Composable
    @ExperimentalMaterial3Api
    fun EventTimeDisplay(){
        var showTimePicker by remember { mutableStateOf(false) }
        val time by rememberSaveable { mutableStateOf("12:24")}

        Text("Time: $time")


        if (showTimePicker) {

            Button(onClick={showTimePicker = false}, content={Text(text = "Save")})

        }
        else
        {
            Button(onClick={showTimePicker = true}, content={Text(text = "Set Time")})
        }


    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventInputField(name:String)
    {
        var inputText by rememberSaveable {mutableStateOf("")}
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(name) }
        )
        return inputText
    }



    @Preview
    @Composable
    fun CreateEventMenuPreview(){
        CreateEventMenu()
    }


    @Preview
    @Composable
    fun EventDisplayPreview(){
        val ev = Event("title","desc","loc","time","date")
        EventDisplay(ev)
    }

