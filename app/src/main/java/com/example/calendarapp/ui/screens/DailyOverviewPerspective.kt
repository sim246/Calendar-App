package com.example.calendarapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.calendarapp.R

@Composable
fun DailyOverview(day:String) {

    var hour = 6
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
    {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Image(
                painterResource(id = R.drawable.arrow_left),
                contentDescription ="Cart button icon",
                modifier = Modifier.size(40.dp)
                    .clickable {  })
            Text(day)
            Image(
                painterResource(id = R.drawable.arrow_right),
                contentDescription ="Cart button icon",
                modifier = Modifier.size(40.dp)
                    .clickable {  })
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
            Image(
                painterResource(id = R.drawable.add_button),
                contentDescription ="Cart button icon",
                modifier = Modifier.size(40.dp)
                .clickable {  })
        }
        Spacer(modifier = Modifier.height(5.dp))
        var i = 0
        var color: Color
        while (i <= 18)
        {
            color = if (i % 2 == 0) {
                MaterialTheme.colorScheme.outlineVariant
            } else {
                MaterialTheme.colorScheme.onPrimary
            }
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(50.dp)
                    .border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline))
                    .fillMaxWidth()
                    .background(color)
            )
            {
                Text("$hour:00", color = MaterialTheme.colorScheme.scrim)
            }
            i++
            if (hour == 12)
            {
                hour = 1
            } else
            {
                hour++
            }
        }
    }
}