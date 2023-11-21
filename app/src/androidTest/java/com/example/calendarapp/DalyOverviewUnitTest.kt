package com.example.calendarapp

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.ui.presentation.screens.DailyOverview
import com.example.calendarapp.ui.presentation.screens.ScreenSetup
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import androidx.activity.viewModels
import com.example.calendarapp.ui.theme.CalendarAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@RunWith(AndroidJUnit4::class)
class DalyOverviewUnitTest {
    @get: Rule
    val composeTestRule = createComposeRule()


    //Call the MySimpleUI function
    @Test
    fun setUP() {
        composeTestRule.setContent {
            CalendarAppTheme {
                val viewModel:AppViewmodel = AppViewmodel()
                ScreenSetup(viewModel)
            }
        }
    }

    @Test
    fun verifyIfAllViewsIsDisplayed() {
        composeTestRule.onNodeWithTag("Click Event Display", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Click Back", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Click Add", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Next Day", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Previous Day", useUnmergedTree = true).assertIsDisplayed()
    }
}