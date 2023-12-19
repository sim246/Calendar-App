package com.example.calendarapp

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.calendarapp.ui.presentation.screens.AppViewmodelFactory
import com.example.calendarapp.ui.presentation.screens.ScreenSetup
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.google.android.gms.location.LocationServices
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class MonthOverviewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
    fun monthOverviewNavigationTest() {
        composeTestRule.onRoot().printToLog("TAG")


        //initial November screen
        composeTestRule.onNodeWithText("NOVEMBER 2023", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .assertIsDisplayed()


        //previous month, should show october
        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
            .performClick()
        composeTestRule.onNodeWithText("OCTOBER 2023", useUnmergedTree = true).assertIsDisplayed()


        //next month, after october, should show november
        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .performClick()
        composeTestRule.onNodeWithText("NOVEMBER 2023", useUnmergedTree = true).assertIsDisplayed()

    }

    @Test
    fun monthOverviewNumberOfDaysTest() {
        //november is 30 days
        composeTestRule.onNodeWithText("30", useUnmergedTree = true).assertIsDisplayed()

        //december is 31 days
        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .performClick()
        composeTestRule.onNodeWithText("31", useUnmergedTree = true).assertIsDisplayed()

    }


}
