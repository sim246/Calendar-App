package com.example.calendarapp.ui.presentation.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
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
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.domain.Holiday
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun DailyOverview(searchResults: List<Event>, holidays: List<Holiday>?, viewModel: AppViewmodel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("DailyOverviewUI")
    )
    {
        TopHalf(holidays, navController, viewModel)
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
                LazyColumn (modifier = Modifier.fillMaxWidth().height(1080.dp)){
                    viewModel.findEventsByDay(viewModel.currentDay)
                    item {
                        ScheduleDisplay(searchResults, navController, viewModel)
                    }
                }
            }
        }
    }
}

val EventTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
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
            text = "${event.start.format(EventTimeFormatter)} - ${event.theEnd.format(
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

@Composable
fun ScheduleDisplay(events : List<Event>?, navController: NavController, viewModel: AppViewmodel) {
    events?.sortedBy(Event::start)?.forEach { event ->
        if (event.day == viewModel.currentDay) {
            val height = (event.theEnd.hour - event.start.hour) * 60
            val heightMin =
                event.theEnd.minute - event.start.minute
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
                    var y = event.start.hour
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

val DayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM uuuu")
@Composable
fun TopHalf(
    holidays: List<Holiday>?,
    navController: NavController,
    viewModel: AppViewmodel
) {
    val day = viewModel.currentDay.format(DayFormatter).toString()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BackwardsArrowButton(
            navController = navController,
            viewModel = viewModel
        )
        Column {
            Text(
                day,
                modifier = Modifier.height(50.dp),
                fontSize = 20.sp,
                color = Color.Black
            )
            if(viewModel.currentDay.toLocalDate() == LocalDate.now()){
                //show button for forecast
                val weather = viewModel.getCurrentDayForecast(viewModel.utilityHelper)
                if(weather != null){
                    Button(onClick = { /*TODO*/ }) {
                        Text(weather.condition + " - " + weather.temperature)
                    }
                }
                else
                {
                    Text("Weather not found, try again later.")
                }
            }
            if (holidays != null) {
                for (i in holidays.indices) {
                    if (viewModel.currentDay.toLocalDate().toString() == holidays[i].date && holidays[0].types[0] == "Public") {
                        Text(holidays[i].name, color = Color.LightGray)
                    }
                }
            }
        }
        ForwardArrowButton(
            navController = navController,
            viewModel = viewModel
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
    AddButton(navController = navController, viewModel)
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun AddButton(navController: NavController, viewModel: AppViewmodel) {
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
                        viewModel.setCurrentEvent(
                            Event(
                                viewModel.currentDay,
                                "",
                                LocalDateTime.now(),
                                LocalDateTime.now(),
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
    navController: NavController,
    viewModel: AppViewmodel
) {
    IconButton(
        onClick = {
            val date: LocalDateTime =
                viewModel.currentDay
                .plusDays(1)
            viewModel.setNewDay(date)
            navController.navigate(Routes.DailyOverview.route)
        },
        modifier = Modifier.testTag("Next Day"),
    ) {
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Day")
    }
}

@Composable
fun BackwardsArrowButton(
    navController: NavController,
    viewModel: AppViewmodel
) {
    IconButton(
        onClick = {
            val date: LocalDateTime =
                viewModel.currentDay
                .minusDays(1)
            viewModel.setNewDay(date)
            navController.navigate(Routes.DailyOverview.route)
        },
        modifier = Modifier.testTag("Previous Day")
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Day")
    }
}