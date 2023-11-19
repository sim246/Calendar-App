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
import com.example.calendarapp.ui.resources.AppViewmodel
import com.example.calendarapp.ui.screens.DailyOverview
import com.example.calendarapp.ui.screens.MonthOverviewScreen
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.example.calendarapp.ui.screens.SingleEventDisplay
import com.example.calendarapp.ui.screens.SingleEventEdit

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
                    val viewModel = AppViewmodel()
                    val context = LocalContext.current
                    ScreenSetup(context, viewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenSetup(context: Context, appViewmodel: AppViewmodel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MonthOverviewScreen.route)
    {
        composable(Routes.MonthOverviewScreen.route) {
            MonthOverviewScreen(navController = navController, context, appViewmodel)
        }
        composable(Routes.DailyOverview.route) {
            DailyOverview(appViewmodel, navController)
        }
        composable(Routes.EventOverview.route) {
            SingleEventDisplay(appViewmodel.currentlyViewingEvent, navController, appViewmodel)
        }
        composable(Routes.EventEdit.route) {
            SingleEventEdit(appViewmodel.currentlyViewingEvent, navController, appViewmodel)
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalendarAppTheme {
        val viewModel = AppViewmodel()
        val context = LocalContext.current
        ScreenSetup(context, viewModel)
    }
}