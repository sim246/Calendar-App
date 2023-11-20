package com.example.calendarapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calendarapp.ui.resources.AppViewmodel

import com.example.calendarapp.ui.screens.App
import com.example.calendarapp.ui.screens.AddEvent
import com.example.calendarapp.ui.screens.DailyOverview
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.example.calendarapp.ui.screens.SingleEventDisplay
import com.example.calendarapp.ui.screens.SingleEventEdit

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewmodel by viewModels()
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
                    ScreenSetup(viewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenSetup(appViewmodel: AppViewmodel) {
    val navController = rememberNavController()

    val holidays by appViewmodel.holidays.observeAsState(null)

    LaunchedEffect(Unit) {
        appViewmodel.fetchHolidays()
    }

    NavHost(navController = navController, startDestination = Routes.MonthOverviewScreen.route)
    {
        composable(Routes.MonthOverviewScreen.route) {
            App(holidays, navController = navController, appViewmodel)
        }
        composable(Routes.DailyOverview.route) {
            DailyOverview(holidays, appViewmodel, navController)
        }
        composable(Routes.EventOverview.route) {
            SingleEventDisplay(appViewmodel.currentlyViewingEvent, navController, appViewmodel)
        }
        composable(Routes.EventEdit.route) {
            SingleEventEdit(appViewmodel.currentlyViewingEvent, navController, appViewmodel)
        }

        composable(Routes.AddEvent.route) {
            AddEvent(appViewmodel.currentlyViewingEvent,navController, appViewmodel)
        }
    }
}