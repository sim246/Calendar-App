package com.example.calendarapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarapp.R
import com.example.calendarapp.ui.resources.Event
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyOverview(day:String,events: List<Event>?) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
    {
        TopHalf(day)
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
                    .offset(x = 50.dp, y = 0.dp)
            ) {
                if (!events.isNullOrEmpty()) {
                    ScheduleDisplay(events)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
val EventTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDisplay(event: Event) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(4.dp))
                .padding(4.dp)
                .fillMaxHeight()
                .width(250.dp)
        ) {
            Text(
                text = "${event.start.format(EventTimeFormatter)} - ${event.end.format(EventTimeFormatter)}",
            )

            Text(
                text = event.eventName,
                fontWeight = FontWeight.Bold,
            )
        }

}

@RequiresApi(Build.VERSION_CODES.O)
val FormatterHours: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
@RequiresApi(Build.VERSION_CODES.O)
val FormatterMinuts: DateTimeFormatter = DateTimeFormatter.ofPattern("mm")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleDisplay(events: List<Event>){
    Column(modifier = Modifier.fillMaxSize()) {
        events.sortedBy(Event::start).forEach { event ->
            val height = (event.end.format(FormatterHours).toInt() - event.start.format(FormatterHours).toInt()) * 50
            Log.d("height", (event.end.format(FormatterMinuts).toInt()).toString())
            Layout(
                content = { EventDisplay(event) }
            ) { measureables, constraints ->
                val placeables = measureables.map { measurable ->
                    measurable.measure(constraints.copy(maxHeight = (height + event.end.format(FormatterMinuts).toInt() - 5).dp.roundToPx()))
                }
                layout(constraints.maxWidth, height) {
                    var y = (event.start.format(FormatterHours).toInt()) * 15
                    if (event.start.format(FormatterHours).toInt() > 12){
                        y = ((event.start.format(FormatterHours).toInt()) * 25)
                    }
                    placeables.forEach { placeable ->
                        placeable.place(0, y)
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
                MaterialTheme.colorScheme.onPrimary
            } else {
                Color.LightGray
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(50.dp)
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
fun TopHalf(day:String){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
        Image(
            painterResource(id = R.drawable.arrow_left),
            contentDescription ="Cart button icon",
            modifier = Modifier
                .size(50.dp)
                .clickable { })
        Text(day, modifier = Modifier.height(50.dp), fontSize = 20.sp)
        Image(
            painterResource(id = R.drawable.arrow_right),
            contentDescription ="Cart button icon",
            modifier = Modifier
                .size(50.dp)
                .clickable { })
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
        Image(
            painterResource(id = R.drawable.add_button),
            contentDescription ="Cart button icon",
            modifier = Modifier
                .size(50.dp)
                .clickable { })
    }
    Spacer(modifier = Modifier.height(5.dp))
}