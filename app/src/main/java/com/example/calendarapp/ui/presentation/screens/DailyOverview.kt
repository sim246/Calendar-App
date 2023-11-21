package com.example.calendarapp.ui.presentation.screens

import android.os.Build

import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.data.retrofit.Holiday
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
        TopHalf(holidays, viewModel.currentDay.toString(), navController, viewModel)
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
                for (i in viewModel.events.indices) {
                    if (viewModel.events.size > 0 && viewModel.events[i].day == viewModel.currentDay) {
                        ScheduleDisplay(viewModel.events, navController, viewModel)
                    }
                }
                //filter events by current day
                val filteredEvents = viewModel.events.filter { ev -> ev.day == viewModel.currentDay}

               // if (viewModel.events.size > 0 && viewModel.events[0].day == viewModel.currentDay) {
                    ScheduleDisplay(filteredEvents, navController, viewModel)
               // }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
val EventTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDisplay(event: Event, navController: NavController, viewModel: AppViewmodel, height:Int) {
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
                text = "${event.start.format(EventTimeFormatter)} - ${event.end.format(
                    EventTimeFormatter
                )}",
                color = Color.White
            )

            Text(
                text = event.eventName,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

}

@RequiresApi(Build.VERSION_CODES.O)
val FormatterHours: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
@RequiresApi(Build.VERSION_CODES.O)
val FormatterMin: DateTimeFormatter = DateTimeFormatter.ofPattern("mm")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleDisplay(events : List<Event>, navController: NavController, viewModel: AppViewmodel) {

    Column(modifier = Modifier.fillMaxSize()) {

        events.sortedBy(Event::start).forEach { event ->
            val height = (event.end.format(FormatterHours).toInt() - event.start.format(
                FormatterHours
            ).toInt()) * 60
            val heightMin =
                event.end.format(FormatterMin).toInt() - event.start.format(FormatterMin).toInt()
            Layout(
                content = { EventDisplay(event, navController, viewModel, (height +  heightMin)) }
            ) { measureables, constraints ->
                val placeables = measureables.map { measurable ->
                    measurable.measure(constraints.copy(maxHeight = (height + heightMin).dp.roundToPx()))
                }
                layout(constraints.maxWidth, height) {
                    var y = event.start.format(FormatterHours).toInt()
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TopHalf(
        holidays: List<Holiday>?,
        day: String,
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
                    day,
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AddButton(navController: NavController, day: String, viewModel: AppViewmodel) {
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
                        if (LocalDateTime.now()
                                .format(Formatter) <= viewModel.currentDay.toString()
                        ) {
                            //Create a new (empty) event for the selected day,
                            // set it to the currently viewing one
                            // and open the edit menu for it
                            viewModel.isEditing = false
                            viewModel.setCurrentEvent(
                                Event(
                                    LocalDate.parse(day),
                                    "",
                                    LocalDateTime.now(),
                                    LocalDateTime.now()
                                )
                            )
                            navController.navigate(Routes.EventEdit.route)
                        }
                        //Create a new (empty) event for the selected day,
                        // set it to the currently viewing one
                        // and open the edit menu for it
                        viewModel.isEditing = false
                        viewModel.setCurrentEvent(
                            Event(
                                LocalDate.parse(day), "", LocalDate.parse(day).atTime(
                                    LocalTime.now()
                                ), LocalDate.parse(day).atTime(LocalTime.now())
                            )
                        )
                        navController.navigate(Routes.EventEdit.route)
                    }
                    .testTag("Click Add")
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ForwardArrowButton(
        day: String,
        navController: NavController,
        viewModel: AppViewmodel
    ) {
        IconButton(
            onClick = {
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date: LocalDate = LocalDate
                    .parse(day, format)
                    .plusDays(1)
                viewModel.setNewDay(date)
                navController.navigate(Routes.DailyOverview.route)
            },
            modifier = Modifier.testTag("Next Day")
        ) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Day")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun BackwardsArrowButton(
        day: String,
        navController: NavController,
        viewModel: AppViewmodel
    ) {
        IconButton(
            onClick = {
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date: LocalDate = LocalDate
                    .parse(day, format)
                    .minusDays(1)
                viewModel.setNewDay(date)
                navController.navigate(Routes.DailyOverview.route)
            },
            modifier = Modifier.testTag("Previous Day")
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Day")
        }
    }



