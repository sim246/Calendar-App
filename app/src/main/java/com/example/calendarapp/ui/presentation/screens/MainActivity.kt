package com.example.calendarapp.ui.presentation.screens

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.presentation.viewmodel.UtilityHelper
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            CalendarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner = LocalViewModelStoreOwner.current

                    owner?.let {
                        val viewModel: AppViewmodel = viewModel(
                            it,
                            "AppViewmodel",
                            AppViewmodelFactory(
                                LocalContext.current.applicationContext
                                        as Application, LocalContext.current, fusedLocationClient
                            )
                        )
                        ScreenSetup(viewModel)
                    }
                }
            }
        }
    }
}

    @Composable
    fun ScreenSetup(appViewmodel: AppViewmodel) {
        Log.d("WeatherDownloader", LocalDate.now().toString())
        val navController = rememberNavController()
        val holidays by appViewmodel.holidays.observeAsState(null)
        val allEvents by appViewmodel.allEvents.observeAsState(listOf())
        val searchResults by appViewmodel.searchResults.observeAsState(listOf())
        LaunchedEffect(Unit) {
            appViewmodel.fetchHolidays()
        }

        NavHost(navController = navController, startDestination = Routes.MonthOverviewScreen.route)
        {
            composable(Routes.MonthOverviewScreen.route) {
                App(allEvents = allEvents,navController = navController, appViewmodel)
            }
            composable(Routes.DailyOverview.route) {
                DailyOverview(searchResults = searchResults,holidays, appViewmodel, navController)
            }
            composable(Routes.EventOverview.route) {
                appViewmodel.currentlyViewingEvent?.let { it1 ->
                    SingleEventDisplay(
                        it1,
                        navController,
                        appViewmodel
                    )
                }
            }
            composable(Routes.EventEdit.route) {
                appViewmodel.currentlyViewingEvent?.let { it1 ->
                    SingleEventEdit(
                        allEvents,
                        it1,
                        navController,
                        appViewmodel
                    )
                }
            }
            composable(Routes.WeatherSingle.route) {
                WeatherSingleDay(appViewmodel)
            }
            composable(Routes.WeatherFive.route) {
                WeatherCurrentDay(appViewmodel)
            }
        }
    }

class AppViewmodelFactory(private val application: Application, private val context: Context,  private val fusedLocationProvider: FusedLocationProviderClient) :
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AppViewmodel(application, UtilityHelper(context), fusedLocationProvider) as T
        }
    }