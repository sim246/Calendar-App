package com.example.calendarapp.ui.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calendarapp.R
import com.example.calendarapp.ui.data.retrofit.Holiday
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

@Composable
fun DailyOverview(holidays: List<Holiday>?, viewModel: AppViewmodel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("DailyOverviewUI")
    )
    {
        TopHalf(holidays, viewModel.currentDay, navController, viewModel)
        Row (Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp)
            ) {
                HourDisplay()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val events = viewModel.findEventByDay(viewModel.currentDay)
                ScheduleDisplay(events, navController, viewModel)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun EventDisplay(event: Event, navController: NavController, viewModel: AppViewmodel, height:Int) {

    val sdf = SimpleDateFormat("hh:mm")
    val start = sdf.format(event.start)
    val end = sdf.format(event.theEnd)

    Column(
        modifier = Modifier
            .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
            .fillMaxWidth()
            .height(height.dp)
            .clickable {
                viewModel.setCurrentEvent(event)
                navController.navigate(Routes.EventOverview.route)
            }
            .testTag("Click Event Display " + event.eventName)
    ) {
        Text(
            text = "$start - $end",
            color = Color.White
        )

        Text(
            text = event.eventName,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}

@Composable
fun ScheduleDisplay(events : List<Event>?, navController: NavController, viewModel: AppViewmodel) {

    Column(modifier = Modifier.fillMaxSize()) {
        events?.sortedBy(Event::start)?.forEach { event ->
            val calStart: Calendar = Calendar.getInstance()
            calStart.time = event.start
            val calEnd: Calendar = Calendar.getInstance()
            calEnd.time = event.theEnd

            val height = (calEnd.get(Calendar.HOUR_OF_DAY) + calStart.get(Calendar.HOUR_OF_DAY)) * 60
            val heightMin = calEnd.get(Calendar.MINUTE) - calStart.get(Calendar.HOUR_OF_DAY)
            Layout(
                content = {
                    EventDisplay(
                        event,
                        navController,
                        viewModel,
                        (height + heightMin)
                    )
                }
            ) { measureables, constraints ->
                val placeables = measureables.map { measurable ->
                    measurable.measure(constraints.copy(maxHeight = (height + heightMin).dp.roundToPx()))
                }
                layout(constraints.maxWidth, height) {
                    var y = calStart.get(Calendar.HOUR_OF_DAY)
                    y -= if (y > 12) {
                        7
                    } else {
                        6
                    }
                    placeables.forEach { placeable ->
                        placeable.place(0, ((y * 60) + heightMin).dp.roundToPx())
                    }
                }
            }
        }
    }
}

@Composable
fun HourDisplay() {
    var i = 0
    var hour = 6
    var color: Color
    Column(modifier = Modifier.fillMaxSize()) {
        while (i <= 18) {
            color = if (i % 2 == 0) {
                Color.LightGray
            } else {
                Color.White
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(color)
            )
            {
                Text("$hour:00", color = MaterialTheme.colorScheme.scrim)
            }
            i++
            if (hour == 12) {
                hour = 1
            } else {
                hour++
            }
        }
    }
}

@Composable
fun TopHalf(
    holidays: List<Holiday>?,
    day: Date,
    navController: NavController,
    viewModel: AppViewmodel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BackwardsArrowButton(
            day = day,
            navController = navController,
            viewModel = viewModel
        )
        Column {
            Text(
                day.toString(),
                modifier = Modifier.height(50.dp),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.scrim
            )
            if (holidays != null) {
                for (i in holidays.indices) {
                    if (viewModel.currentDay.toString() == holidays[i].date) {
                        Text(holidays[i].name)
                    }
                }
            }
        }
        ForwardArrowButton(
            day = day,
            navController = navController,
            viewModel = viewModel
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
    AddButton(navController = navController, day = day, viewModel)
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun AddButton(navController: NavController, day: Date, viewModel: AppViewmodel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painterResource(id = R.drawable.back_arrow),
            contentDescription = "back button icon",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navController.navigate(Routes.MonthOverviewScreen.route)
                }
                .testTag("Click Back")
        )
        Image(
            painterResource(id = R.drawable.add_button),
            contentDescription = "add button icon",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    if (LocalDateTime
                            .now()
                            .format(Formatter) <= viewModel.currentDay.toString()
                    ) {
                        //Create a new (empty) event for the selected day,
                        // set it to the currently viewing one
                        // and open the edit menu for it
                        viewModel.isEditing = false
                        viewModel.setCurrentEvent(
                            Event(
                                day,
                                "",
                                day,
                                day,
                                "",
                                "",
                                ""
                            )
                        )
                        navController.navigate(Routes.EventEdit.route)
                    }
                }
                .testTag("Click Add")
        )
    }
}

@Composable
fun ForwardArrowButton(
    day: Date,
    navController: NavController,
    viewModel: AppViewmodel
) {
    IconButton(
        onClick = {
            val c = Calendar.getInstance()
            c.time = day
            c.add(Calendar.DATE, 1)
            val date = c.time

            viewModel.setNewDay(date)
            navController.navigate(Routes.DailyOverview.route)
        },
        modifier = Modifier.testTag("Next Day")
    ) {
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Day")
    }
}

@Composable
fun BackwardsArrowButton(
    day: Date,
    navController: NavController,
    viewModel: AppViewmodel
) {
    IconButton(
        onClick = {
            val c = Calendar.getInstance()
            c.time = day
            c.add(Calendar.DATE, -1)
            val date = c.time
            viewModel.setNewDay(date)
            navController.navigate(Routes.DailyOverview.route)
        },
        modifier = Modifier.testTag("Previous Day")
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Day")
    }
}