package com.example.calendarapp

import android.os.Build
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
import com.example.calendarapp.ui.theme.CalendarAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@RunWith(AndroidJUnit4::class)
class DalyOverviewUnitTest {
    @JvmField
    @get: Rule
    val composeTestRule = createComposeRule()

    //Call the MySimpleUI function
    fun setUP() {
        composeTestRule.setContent {
            CalendarAppTheme {
                val viewModel = AppViewmodel()
                ScreenSetup(viewModel)
            }
        }
    }
}