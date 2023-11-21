package com.example.calendarapp.ui.screens

import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


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


            var startEndTimes = EventTimeDisplay(event)

            Button(
                content={Text(text = "Save Event")},
                //should save the event at the specified date and time onclick
                onClick={
                    Log.i("EventParams", titleString)
                    //set event values after checking time validity
                    val check = viewModel.checkConflictingEvents(event.start, event.end)
                    if(check == null){
                        event.eventName = titleString
                        event.description = descriptionString
                        event.clientName = clientString
                        event.location = locationString
                        event.start = startEndTimes[0]
                        event.end = startEndTimes[1]
                        Log.d("nya", viewModel.isEditing.toString())
                        if(!viewModel.isEditing)
                        {

                            if(viewModel.addEvent(event)){
                                navController.popBackStack()
                            }
                            else {
                                val toastText = "Something went wrong when adding the event."
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
                    else
                    {
                        val toastText = "Error: $check"
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                    }})

            Button(
                content={Text(text = "Quit without saving")},
                onClick={
                    //isEditing = false
                    navController.popBackStack()
                }
            )

        }




    }





    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SingleEventDisplay(event: Event, navController: NavController, viewModel: AppViewmodel){
        Column{
            Text(text=event.eventName)
            Text(text="@ " + event.location)
            Text(text=event.start.toLocalTime().toString() + " to " + event.end.toLocalTime().toString())
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
                //realistically should have a "Are you sure??" dialog
                onClick={
                    if(viewModel.deleteEvent(event)){
                        navController.popBackStack()
                    }
                }
            )
            Button(
                content={Text(text="Return")},
                onClick = {navController.popBackStack()}
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    @ExperimentalMaterial3Api
    fun EventTimeDisplay(event: Event) : Array<LocalDateTime>{
        //Time Picker declatation

        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        var startTime by remember { mutableStateOf(event.start.toLocalTime()) }
        var endTime by remember { mutableStateOf(event.end.toLocalTime())}
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

        // Fetching current hour, and minute
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]


        val timePickerStart = TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                startTime = LocalTime.parse("$selectedHour:$selectedMinute", formatter)
            }, hour, minute, false
        )
        val timePickerEnd = TimePickerDialog(context,
            { _, selectedHour: Int, selectedMinute: Int ->

                endTime = LocalTime.parse("$selectedHour:$selectedMinute", formatter)
            }, hour, minute, false
        )

            Text(text= "Start Time: $startTime")
            Text(text= "End Time: $endTime")
            Row(){
                Button(onClick={
                    timePickerStart.show()
                }, content={Text(text = "Set Start Time")})
                Button(onClick={
                    timePickerEnd.show()
                }, content={Text(text = "Set End Time")})
            }
        return arrayOf(LocalDateTime.of(event.start.toLocalDate(), startTime), LocalDateTime.of(event.start.toLocalDate(), startTime))

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

