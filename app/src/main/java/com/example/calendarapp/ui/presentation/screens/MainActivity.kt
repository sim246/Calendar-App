package com.example.calendarapp.ui.presentation.screens

import android.app.Application
import android.os.Bundle
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
//import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.ui.presentation.routes.Routes
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                        as Application
                            )
                        )

                        ScreenSetup(viewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun ScreenSetup(appViewmodel: AppViewmodel) {
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
                DailyOverview(allEvents = allEvents, searchResults = searchResults,holidays, appViewmodel, navController)
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
                        it1,
                        navController,
                        appViewmodel
                    )
                }
            }
        }
    }
}

    class AppViewmodelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AppViewmodel(application) as T
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