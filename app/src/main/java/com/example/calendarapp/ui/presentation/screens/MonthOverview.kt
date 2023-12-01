package com.example.calendarapp.ui.presentation.screens

import android.util.Log
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
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.google.android.libraries.places.api.model.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@Composable
fun MonthOverviewScreen(navController: NavController, viewModel: AppViewmodel) {
    YearAndNav(navController, viewModel)

}

@Composable
fun YearAndNav(navController: NavController, viewModel: AppViewmodel) {
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
                text = "${selectedMonth.month.name} ${selectedMonth.year}",
                modifier = Modifier
                    .testTag("NOVEMBER 2023")
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

        DaysOfTheWeek(selectedMonth = selectedMonth, navController, viewModel)
    }
}

@Composable
fun DaysOfTheWeek(selectedMonth: YearMonth, navController: NavController, viewModel: AppViewmodel){
    // Days of the week
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (day in DayOfWeek.values()) {
            Text(
                text = day.name.take(3),
                modifier = Modifier.weight(1f)
            )
        }
    }

    DaysOfTheMonth(selectedMonth = selectedMonth, navController, viewModel)
}

val Formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
@Composable
fun DaysOfTheMonth(selectedMonth: YearMonth, navController: NavController, viewModel: AppViewmodel) {

    val daysWithEvents = viewModel.getDaysWithEvents(selectedMonth)
    Log.d("Daysofthemonth", daysWithEvents.toString())

    // Days in the month
    val daysInMonth = selectedMonth.lengthOfMonth()
    val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value % 7 + 1
    val currentDay = LocalDate.now().dayOfMonth
    val rows = ((daysInMonth + firstDayOfWeek - 1 + 6) / 7)

    for (row in 0 until rows) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (col in 1..7) {
                val day = row * 7 + col - firstDayOfWeek + 1
                var color = Color.White
                var fontColor = Color.Black

                 if (day in 1..daysInMonth && daysWithEvents != null) {

                     val cal: Calendar = Calendar.getInstance()
                     cal.set(Calendar.YEAR, selectedMonth.year)
                     cal.set(Calendar.MONTH, selectedMonth.monthValue)
                     cal.set(Calendar.DAY_OF_MONTH, day)
                     val date: Date = cal.time

                     val hasEvent:Boolean = daysWithEvents.contains(date)
                     if (hasEvent) {
                         color = Color.DarkGray
                         fontColor = Color.White
                     }
                     if(day == currentDay){
                         color = Color.LightGray
                         fontColor = Color.White
                     }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .background(color)
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                viewModel.setNewDay(date)
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
fun App(navController: NavController, viewModel: AppViewmodel) {
    MonthOverviewScreen(navController, viewModel)
}