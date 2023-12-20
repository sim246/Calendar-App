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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.calendarapp.R
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.domain.Event
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleEventEdit(
    allEvents:List<Event>,
    event: Event,
    navController: NavController,
    viewModel: AppViewmodel
){

    Column (modifier = Modifier.testTag("EventEditOverviewUI")){
        val titleString = eventInputField(stringResource(R.string.title_required), event.eventName)
        val descriptionString = event.description?.let { eventInputField(stringResource(R.string.description), it) }
        val locationString = event.location?.let { eventInputField(stringResource(R.string.location), it) }
        val clientString = event.clientName?.let { eventInputField(stringResource(R.string.client_name), it) }
        // Fetching local context
        val context = LocalContext.current
        val startEndTimes = eventTimeDisplay(event)

        Row (modifier = Modifier.testTag("Save")) {
            Button(
                content = { Text(text = stringResource(R.string.save_event)) },
                //should save the event at the specified date and time onclick
                onClick = {
                    Log.i("nya title", titleString)
                    Log.i("nya", startEndTimes[0].toString())
                    Log.i("nya", startEndTimes[1].toString())
                    //set event values after checking time validity
                    if (titleString.isNotEmpty()) {
                        val check = viewModel.checkConflictingEvents(
                            event.id,
                            startEndTimes[0],
                            startEndTimes[1],
                            allEvents
                        )
                        if (check == null) {
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
        }
        Row (modifier = Modifier.testTag("No Save")) {
            Button(
                content = { Text(text = stringResource(R.string.quit_without_saving)) },
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun SingleEventDisplay(event: Event, navController: NavController, viewModel: AppViewmodel){
    Column (modifier = Modifier.testTag("EventDisplayOverviewUI")){
        Text(text=event.eventName)
        Text(text="@ " + event.location)
        Text(text=event.start.toLocalTime().toString() + " to " + event.theEnd.toLocalTime().toString())
        event.description?.let { Text(text= it) }
        Row (modifier = Modifier.testTag("Edit")) {
            Button(
                content = { Text(text = stringResource(R.string.edit_event)) },
                onClick = {
                    navController.navigate(Routes.EventEdit.route)
                }
            )
        }
        Row (modifier = Modifier.testTag("Delete")) {
            Button(
                content = { Text(text = stringResource(R.string.delete_event)) },
                //realistically should have a "Are you sure??" dialog
                onClick = {
                    viewModel.deleteEvent(event.id)
                    navController.navigate(Routes.DailyOverview.route)
                }
            )
        }
        Row (modifier = Modifier.testTag("Return")) {
            Button(
                content = { Text(text = stringResource(R.string.return_btn)) },
                onClick = {
                    navController.navigate(Routes.DailyOverview.route)
                }
            )
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
@ExperimentalMaterial3Api
fun eventTimeDisplay(event: Event) : Array<LocalDateTime>{
    //Time Picker declatation
    val context = LocalContext.current
    var startTime by remember { mutableStateOf(fixString(event.start.hour.toString()) + ":"+ fixString(event.start.minute.toString())) }
    var endTime by remember { mutableStateOf(fixString(event.theEnd.hour.toString()) + ":"+ fixString(event.theEnd.minute.toString())) }
    val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

    // Fetching current hour, and minute
    //val hour = calendar[Calendar.HOUR_OF_DAY] + 1
    //val minute = calendar[Calendar.MINUTE]
    //Log.d("hour", hour.toString())
    //Log.d("minute", minute.toString())


    val timePickerStart = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            //startTime = fixString(selectedHour.toString()) + ":" + fixString(selectedMinute.toString())
            startTime = formatTime(selectedHour, selectedMinute)
        }, 11, 0, false
    )
    val timePickerEnd = TimePickerDialog(context,
        { _, selectedHour: Int, selectedMinute: Int ->
            //endTime = fixString(selectedHour.toString()) + ":" + fixString(selectedMinute.toString())
            endTime = formatTime(selectedHour, selectedMinute)
        }, 13, 0, false
    )
    Text(
        //text= "Start Time: $startTime"
        //text = stringResource(R.string.start_time, startTime)
        text = stringResource(R.string.start_time, formatLocalizedTime(startTime))

    )
    Text(
        //text= "End Time: $endTime"
        //text = stringResource(R.string.end_time, endTime)
        text = stringResource(R.string.end_time, formatLocalizedTime(endTime))

    )
    Row{
        Column(modifier = Modifier.testTag("Start")) {
            Button(onClick = {
                timePickerStart.show()
            }, content = { Text(text = stringResource(R.string.set_start_time)) })
        }
        Column(modifier = Modifier.testTag("End")) {
            Button(onClick = {
                timePickerEnd.show()
            }, content = { Text(text = stringResource(R.string.set_end_time)) })
        }
    }
    //fix strings for parsings
    return arrayOf(LocalDateTime.of(event.day.toLocalDate(), LocalTime.parse(startTime, formatter)),
        LocalDateTime.of(event.day.toLocalDate(), LocalTime.parse(endTime, formatter)))

}


private fun formatTime(hour: Int, minute: Int): String {
    return fixString(hour.toString()) + ":" + fixString(minute.toString())
}

private fun formatLocalizedTime(time: String): String {
    val formatter = DateTimeFormatter.ofPattern("H:mm")
    val localizedFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    return LocalTime.parse(time, formatter).format(localizedFormatter)
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
