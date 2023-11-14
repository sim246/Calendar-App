package com.example.calendarapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calendarapp.ui.resources.Event
import com.example.calendarapp.ui.screens.DailyOverview
import com.example.calendarapp.ui.screens.MonthOverviewScreen
import com.example.calendarapp.ui.theme.CalendarAppTheme
import java.time.LocalDate
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
                    val events: List<Event> = listOf(
                        Event(
                            "event",
                            LocalDateTime.parse("2021-05-18T15:15:00"),
                            LocalDateTime.parse("2021-05-18T16:30:00"),
                            "des",
                            "name",
                            "loc"
                        ),
                        Event(
                            "event",
                            LocalDateTime.parse("2021-05-18T08:15:00"),
                            LocalDateTime.parse("2021-05-18T10:30:00"),
                            "des",
                            "name",
                            "loc"
                        )
                    )
                    val context = LocalContext.current
                    ScreenSetup(context, events)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenSetup(context: Context, events: List<Event>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MonthOverviewScreen.route)
    {
        composable(Routes.MonthOverviewScreen.route) {
            MonthOverviewScreen(navController = navController, context)
        }

        composable(Routes.DailyOverview.route) {
            DailyOverview(LocalDate.parse("2023-11-18"), events = events)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalendarAppTheme {
    val events: List<Event> = listOf(
        Event(
            "event",
            LocalDateTime.parse("2023-11-18T13:15:00"),
            LocalDateTime.parse("2023-11-13T16:30:00"),
            "des",
            "name",
            "loc"
        ),
        Event(
            "event",
            LocalDateTime.parse("2021-05-18T08:15:00"),
            LocalDateTime.parse("2021-05-18T10:30:00"),
            "des",
            "name",
            "loc"
        )
    )
        val context = LocalContext.current
        ScreenSetup(context, events)
    }
}