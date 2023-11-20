package com.example.calendarapp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.calendarapp.ui.presentation.screens.ScreenSetup

import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
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

        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .assertIsDisplayed()


    }
}
