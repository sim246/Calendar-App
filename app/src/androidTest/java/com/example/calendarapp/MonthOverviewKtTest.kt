package com.example.calendarapp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.screens.MonthOverviewScreen
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.calendarapp.ui.resources.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme


@RunWith(AndroidJUnit4::class)
class MonthOverviewKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    fun setUP() {
        composeTestRule.setContent {
            CalendarAppTheme {
                val viewModel = AppViewmodel()
                val context = LocalContext.current
                ScreenSetup(context, viewModel)
            }
        }
    }


    @Test
    fun monthOverviewScreenTest() {

    }
}
