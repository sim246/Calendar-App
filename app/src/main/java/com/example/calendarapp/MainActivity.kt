package com.example.calendarapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendarapp.ui.resources.Event
import com.example.calendarapp.ui.screens.CreateEventMenu
import com.example.calendarapp.ui.screens.DailyOverview
import com.example.calendarapp.ui.theme.CalendarAppTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vent = Event("Example event", LocalDateTime.parse("2021-05-18T15:15:00"), LocalDateTime.parse("2021-05-18T15:15:00"))
                   CreateEventMenu(vent)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalendarAppTheme {
    }
    val events: List<Event> = listOf(Event("event",LocalDateTime.parse("2021-05-18T15:15:00"), LocalDateTime.parse("2021-05-18T16:30:00")), Event("event",LocalDateTime.parse("2021-05-18T08:15:00"), LocalDateTime.parse("2021-05-18T10:30:00")))
    DailyOverview("year-month-day", events = events)
}