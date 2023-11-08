package com.example.calendarapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DailyOverview() {

    var hour = 6;
    var meridiem = "am"
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    )
    {
        var i = 0;
        while (i <= 18)
        {
            Row(horizontalArrangement = Arrangement.Start)
            {
                if (hour != 12) {
                    Text("$hour:00 $meridiem", color = MaterialTheme.colorScheme.primary)
                } else {
                    Text("$hour:00", color = MaterialTheme.colorScheme.primary)
                }
            }
            i++
            if (hour == 12)
            {
                hour = 1
                meridiem = "pm"
            } else
            {
                hour++
            }
        }
    }
}