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
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController

import com.example.calendarapp.ui.resources.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class MonthOverviewKtTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
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
        composeTestRule.onRoot().printToLog("TAG")

        //check if changing the months with the arrow keys leads to the right screen values

        //initial November screen
        composeTestRule.onNodeWithText("NOVEMBER 2023", useUnmergedTree = true).assertIsDisplayed()

        //navigate to the left -- should be october
        composeTestRule.onNodeWithContentDescription("minusMonth")
            .performClick()
        composeTestRule.onNodeWithText("OCTOBER 2023", useUnmergedTree = true).assertIsDisplayed()

        //navigate back to the right -- should be november again
        composeTestRule.onNodeWithContentDescription("plusMonth")
            .performClick()
        composeTestRule.onNodeWithText("NOVEMBER 2023", useUnmergedTree = true).assertIsDisplayed()


        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .assertIsDisplayed()


    }
}
