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
            EventTimePicker()
            Button(
                content={Text(text = "Return to Menu")},
                onClick={}
            )

        }




    }

    @Composable
    fun EventTimePicker(){
        Button(onClick={}, content={Text(text = "Time: ???? pm")},)
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

