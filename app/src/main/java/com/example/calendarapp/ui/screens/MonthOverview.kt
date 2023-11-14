package com.example.calendarapp.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendarapp.Routes
import com.google.android.libraries.places.api.model.DayOfWeek
import java.time.YearMonth
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthOverviewScreen(navController: NavController, context: Context) {
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
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }

            Text(
                text = "${selectedMonth.month.name} ${selectedMonth.year}"
            )

            IconButton(
                onClick = { selectedMonth = selectedMonth.plusMonths(1) }
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

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

        // Days in the month
        val daysInMonth = selectedMonth.lengthOfMonth()
        val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value % 7

        val currentDay = Calendar.getInstance().firstDayOfWeek
        val startDay = ((firstDayOfWeek - currentDay + 8) % 7 + 1) % 7

        val rows = ((daysInMonth + startDay - 1 + 6) / 7)
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (col in 1..7) {
                    val day = row * 7 + col - startDay + 1
                    val isCurrentMonthDay = day in 1..daysInMonth

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .background(Color.Transparent)
                            .clip(MaterialTheme.shapes.small)
                            .clickable { navController.navigate(Routes.DailyOverview.route) }
                    ) {
                        Text(
                            text = if (isCurrentMonthDay) day.toString() else "",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun App() {
        MonthOverviewScreen(navController, context)
    }
}
