package com.example.calendarapp

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.presentation.screens.AppViewmodelFactory
import com.example.calendarapp.ui.presentation.screens.ScreenSetup
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.google.android.gms.location.LocationServices
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RequiresApi(Build.VERSION_CODES.O)
@RunWith(AndroidJUnit4::class)
class DalyOverviewUnitTest {

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    //Call the MySimpleUI function
    @Before
    fun setUP() {
        composeTestRule.setContent {
            CalendarAppTheme {
                val context: Context = LocalContext.current
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

                val owner = LocalViewModelStoreOwner.current
                owner?.let {
                    val viewModelFactory = AppViewmodelFactory(
                        LocalContext.current.applicationContext as Application,
                        LocalContext.current,
                        fusedLocationClient
                    )

                    val viewModel: AppViewmodel =
                        ViewModelProvider(it, viewModelFactory)[AppViewmodel::class.java]

                    ScreenSetup(viewModel)
                }
            }
        }
    }

    @Test
    fun verifyIfAllViewsIsDisplayed() {
        composeTestRule.onNodeWithTag("DailyOverviewUI").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("Click Back", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Click Add", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Next Day", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Previous Day", useUnmergedTree = true).assertIsDisplayed()
    }
}
