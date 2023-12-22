package com.example.calendarapp.ui.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.google.android.libraries.places.api.model.DayOfWeek
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


@Composable
fun MonthOverviewScreen(allEvents: List<Event>, navController: NavController, viewModel: AppViewmodel) {
    YearAndNav(allEvents = allEvents, navController, viewModel)
    viewModel.getCurrentDayForecast()
}

@Composable
fun YearAndNav(allEvents: List<Event>, navController: NavController, viewModel: AppViewmodel) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Month name and navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { selectedMonth = selectedMonth.minusMonths(1) }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Month",
                    modifier = Modifier
                        .testTag("Previous Month")
                        .semantics { contentDescription = "Previous Month" }
                )
            }
            Text(
                text = "${selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${selectedMonth.year}",
                modifier = Modifier.testTag("MONTH 2023")
            )
            IconButton(
                onClick = { selectedMonth = selectedMonth.plusMonths(1) }
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Month",
                    modifier = Modifier
                        .testTag("Next Month")
                        .semantics { contentDescription = "Next Month" }
                )
            }
        }
        DaysOfTheWeek(allEvents = allEvents, selectedMonth = selectedMonth, navController, viewModel)
    }
}

@Composable
fun DaysOfTheWeek(allEvents: List<Event>, selectedMonth: YearMonth, navController: NavController, viewModel: AppViewmodel){
    // Days of the week
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        for (day in DayOfWeek.values()) {

            Text(
                //text = day.name.take(3),
                text = day.getAbbreviatedDisplayName(),
                modifier = Modifier.weight(1f)
            )
        }
    }

    DaysOfTheMonth(allEvents = allEvents, selectedMonth = selectedMonth, navController, viewModel)
}

//Abbreviation of days of the week
fun DayOfWeek.getAbbreviatedDisplayName(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, this.ordinal + 1)
    val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    return dateFormat.format(calendar.time)
}


val Formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
@Composable
fun DaysOfTheMonth(allEvents: List<Event>, selectedMonth: YearMonth, navController: NavController, viewModel: AppViewmodel) {
    // Days in the month
    val daysInMonth = selectedMonth.lengthOfMonth()
    val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value % 7 + 1
    val rows = ((daysInMonth + firstDayOfWeek - 1 + 6) / 7)
    for (row in 0 until rows) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (col in 1..7) {
                val day = row * 7 + col - firstDayOfWeek + 1
                if (day in 1..daysInMonth) {
                    val currentDate = LocalDate.now()
                    val isCurrentDay = selectedMonth.atDay(day) == currentDate
                    var hasEvents:List<LocalDateTime> = mutableListOf()

                    if (allEvents.isNotEmpty()) {
                        val monthsEvents = allEvents.filter {
                            val eventMonth = YearMonth.from(it.day)
                            eventMonth == selectedMonth
                        }
                        val eventDates = monthsEvents.map { it.day }.toSet()
                        hasEvents = eventDates.toList()
                    }
                    Show(
                        day,
                        daysInMonth,
                        hasEvents.contains(selectedMonth.atDay(day).atStartOfDay()),
                        isCurrentDay,
                        selectedMonth,
                        navController,
                        viewModel,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                else {
                    // placeholder for empty spaces in the first and last week
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .background(Color.Transparent)
                    )
                }
            }
        }
    }
}


@Composable
fun Show(
    day: Int,
    daysInMonth: Int,
    hasEvent: Boolean,
    isCurrentDay: Boolean,
    selectedMonth: YearMonth,
    navController: NavController,
    viewModel: AppViewmodel,
    modifier: Modifier
){
    var color = Color.White
    var fontColor = Color.Black

    if (hasEvent) {
        color = Color.DarkGray
        fontColor = Color.White
    }
    else if(isCurrentDay){
        color = Color.LightGray
        fontColor = Color.White
    }
        Box(
            modifier
                .padding(4.dp)
                .background(color)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    val localDateTime: LocalDateTime = selectedMonth.atDay(day).atStartOfDay()
                    viewModel.setNewDay(localDateTime)
                    viewModel.findEventsByDay(viewModel.currentDay)
                    navController.navigate(Routes.DailyOverview.route)
                }
                .semantics { contentDescription = daysInMonth.toString() }
        ) {
            Text(
                text = day.toString(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                color = fontColor
            )
        }
}
@Composable
fun App(allEvents: List<Event>,navController: NavController, viewModel: AppViewmodel) {
    MonthOverviewScreen(allEvents = allEvents,navController, viewModel)
}