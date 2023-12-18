//package com.example.calendarapp
//
//import androidx.compose.ui.test.assertIsDisplayed
//import org.junit.Rule
//import org.junit.Test
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import org.junit.runner.RunWith
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onRoot
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.printToLog
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.calendarapp.ui.presentation.screens.MainActivity
//import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
//import com.example.calendarapp.ui.theme.CalendarAppTheme
//import org.junit.Before
//
//
//@RunWith(AndroidJUnit4::class)
//class MonthOverviewTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Before
//    fun setUP() {
//        composeTestRule.setContent {
//            CalendarAppTheme {
//                val viewModel = AppViewmodel()
//                //UNRESOLVED REFERENCE???
//                //(viewModel)
//
//            }
//        }
//    }
//
//
//
//    @Test
//    fun monthOverviewNavigationTest() {
//        composeTestRule.onRoot().printToLog("TAG")
//
//
//        //initial November screen
//        composeTestRule.onNodeWithText("NOVEMBER 2023", useUnmergedTree = true).assertIsDisplayed()
//
//        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
//            .assertIsDisplayed()
//
//        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
//            .assertIsDisplayed()
//
//
//        //previous month, should show october
//        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
//            .performClick()
//        composeTestRule.onNodeWithText("OCTOBER 2023", useUnmergedTree = true).assertIsDisplayed()
//
//
//        //next month, after october, should show november
//        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
//            .performClick()
//        composeTestRule.onNodeWithText("NOVEMBER 2023", useUnmergedTree = true).assertIsDisplayed()
//
//    }
//
//    @Test
//    fun monthOverviewNumberOfDaysTest() {
//        //november is 30 days
//        composeTestRule.onNodeWithText("30", useUnmergedTree = true).assertIsDisplayed()
//
//        //december is 31 days
//        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
//            .performClick()
//        composeTestRule.onNodeWithText("31", useUnmergedTree = true).assertIsDisplayed()
//
//    }
//
//
//}
