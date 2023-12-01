package com.example.calendarapp.ui.presentation.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme

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

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ScreenSetup(appViewmodel: AppViewmodel) {
    val navController = rememberNavController()

    val holidays by appViewmodel.holidays.observeAsState(null)

    LaunchedEffect(Unit) {
        appViewmodel.fetchHolidays()
    }

    NavHost(navController = navController, startDestination = Routes.MonthOverviewScreen.route)
    {
        composable(Routes.MonthOverviewScreen.route) {
            App(navController = navController, appViewmodel)
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
    }
}

/*
@Preview
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun SimpleComposablePreview() {
    val viewModel = AppViewmodel()
    ScreenSetup(viewModel)
}

 */