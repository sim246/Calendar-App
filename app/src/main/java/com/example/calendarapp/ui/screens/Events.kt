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
            EventInputField("Title")
            EventInputField("Description")
            EventInputField("Location")
            EventTimeDisplay()
            Button(
                content={Text(text = "Return to Menu")},
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

            Button(onClick={showTimePicker = false}, content={Text(text = "Save")},)

        }
        else
        {
            Button(onClick={showTimePicker = true}, content={Text(text = "Set Time")},)
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
    }



    @Preview
    @Composable
    fun CreateEventMenuPreview(){
        CreateEventMenu()
    }

