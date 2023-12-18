package com.example.calendarapp.ui.presentation.screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
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
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.domain.Event
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleEventEdit(
    allEvents:List<Event>,
    event: Event,
    navController: NavController,
    viewModel: AppViewmodel
){

    Column{
        val titleString = eventInputField("Title (Required)", event.eventName)
        val descriptionString = event.description?.let { eventInputField("Description", it) }
        val locationString = event.location?.let { eventInputField("Location", it) }
        val clientString = event.clientName?.let { eventInputField("Client Name", it) }
        // Fetching local context
        val context = LocalContext.current
        val startEndTimes = eventTimeDisplay(event)

        Button(
            content={Text(text = "Save Event")},
            //should save the event at the specified date and time onclick
            onClick={
                Log.i("nya title", titleString)
                Log.i("nya", startEndTimes[0].toString())
                Log.i("nya", startEndTimes[1].toString())
                //set event values after checking time validity
                if(titleString.isNotEmpty()){
                    val check = viewModel.checkConflictingEvents(startEndTimes[0], startEndTimes[1], allEvents)
                    if(check == null) {
                        event.eventName = titleString
                        event.description = descriptionString
                        event.clientName = clientString
                        event.location = locationString
                        event.start = startEndTimes[0]
                        event.theEnd = startEndTimes[1]
                        viewModel.insertEvent(event)
                        navController.navigate(Routes.DailyOverview.route)
                    } else {
                        Toast.makeText(context, "$check", Toast.LENGTH_LONG).show()
                    }
                } else {
                    //on unfilled required fields
                    val toastText = "Please fill all the required values."
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
            })

        Button(
            content={Text(text = "Quit without saving")},
            onClick={
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun SingleEventDisplay(event: Event, navController: NavController, viewModel: AppViewmodel){
    Column{
        Text(text=event.eventName)
        Text(text="@ " + event.location)
        Text(text=event.start.toLocalTime().toString() + " to " + event.theEnd.toLocalTime().toString())
        event.description?.let { Text(text= it) }
        Button(
            content={Text(text = "Edit Event")},
            onClick={
                navController.navigate(Routes.EventEdit.route)
            }
        )
        Button(
            content={Text(text = "Delete Event")},
            //realistically should have a "Are you sure??" dialog
            onClick={
                viewModel.deleteEvent(event.id)
                navController.navigate(Routes.DailyOverview.route)
            }
        )
        Button(
            content={Text(text="Return")},
            onClick = {
                navController.navigate(Routes.DailyOverview.route)
            }
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
@ExperimentalMaterial3Api
fun eventTimeDisplay(event: Event) : Array<LocalDateTime>{
    //Time Picker declatation

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var startTime by remember { mutableStateOf(fixString(event.start.hour.toString()) + ":"+ fixString(event.start.minute.toString())) }
    var endTime by remember { mutableStateOf(fixString(event.theEnd.hour.toString()) + ":"+ fixString(event.theEnd.minute.toString())) }
    val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

    // Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]


    val timePickerStart = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            startTime = fixString(selectedHour.toString()) + ":" + fixString(selectedMinute.toString())
        }, hour, minute, false
    )
    val timePickerEnd = TimePickerDialog(context,
        { _, selectedHour: Int, selectedMinute: Int ->
            endTime = fixString(selectedHour.toString()) + ":" + fixString(selectedMinute.toString())
        }, hour, minute, false
    )
    Text(text= "Start Time: $startTime")
    Text(text= "End Time: $endTime")
    Row{
        Button(onClick={
            timePickerStart.show()
        }, content={Text(text = "Set Start Time")})
        Button(onClick={
            timePickerEnd.show()
        }, content={Text(text = "Set End Time")})
    }
    //fix strings for parsings
    return arrayOf(LocalDateTime.of(event.start.toLocalDate(), LocalTime.parse(startTime, formatter)),
        LocalDateTime.of(event.theEnd.toLocalDate(), LocalTime.parse(endTime, formatter)))

}

fun fixString(input : String): String{
    if(input.length == 1){
        return "0$input"
    }
    return input
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
