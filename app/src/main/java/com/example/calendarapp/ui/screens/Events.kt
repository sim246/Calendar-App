package com.example.calendarapp.ui.screens

import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.calendarapp.Routes
import com.example.calendarapp.ui.resources.AppViewmodel
import com.example.calendarapp.ui.resources.Event
import java.time.LocalDate
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SingleEventEdit(
    event: Event, navController: NavController,
    viewModel: AppViewmodel
){

        Column{
            var titleString = eventInputField("Title", event.eventName)
            var descriptionString = eventInputField("Description", event.description)
            var locationString = eventInputField("Location", event.location)
            var clientString = eventInputField("Client", event.clientName)

            // Fetching local context
            val context = LocalContext.current


            EventTimeDisplay(event)

            Button(
                content={Text(text = "Save Event")},
                //should save the event at the specified date and time onclick
                onClick={
                    Log.i("EventParams", titleString)
                    //set event values
                    event.eventName = titleString
                    event.description = descriptionString
                    event.clientName = clientString
                    event.location = locationString
                    if(!viewModel.isEditing)
                    {

                        if(viewModel.addEvent(event)){
                            navController.popBackStack()
                        }
                        else
                        {
                            val toastText = "Something went wrong when adding the event. " +
                                    "Try checking the times for conflicts."
                            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()

                        }
                    }
                    else
                    {
                        //i'm sure setting the event values to the current
                        //would save it in memory
                        navController.popBackStack()
                    }


                }
            )
            Button(
                content={Text(text = "Quit without saving")},
                onClick={
                    //isEditing = false
                    navController.popBackStack()
                }
            )

        }




    }

    @Composable
    fun TimePickerDigit(){
        //Time picker for a specific digit (hour, minute, second)

    }



    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SingleEventDisplay(event: Event, navController: NavController, viewModel: AppViewmodel){
        Column{
            Text(text=event.eventName)
            Text(text="@ " + event.location)
            Text(text=event.start.toString() + " to " + event.end.toString())
            Text(text=event.description)
            Button(
                content={Text(text = "Edit Event")},
                onClick={
                    viewModel.isEditing = true
                    navController.navigate(Routes.EventEdit.route)
                }
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
    fun eventInputField(name:String, initialValue: String): String {
        var inputText by rememberSaveable {mutableStateOf(initialValue)}
        TextField(
            value = inputText,
            onValueChange = {
                inputText = it

                            },
            label = { Text(name) }
        )
        return inputText
    }

