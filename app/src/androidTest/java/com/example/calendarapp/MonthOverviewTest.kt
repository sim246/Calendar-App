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
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


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

        val currentDate = LocalDate.now()
        val currentMonth = currentDate.month
        val currentMonthCapitalized =
            currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        var currentYear = Year.now().value

        //initial screen
        composeTestRule.onNodeWithText("$currentMonthCapitalized $currentYear", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithTag("MONTH 2023", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .assertIsDisplayed()

        val previousMonth = currentDate.minusMonths(1).month
        val previousMonthCapitalized =
            previousMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        //previous month
        composeTestRule.onNodeWithTag("Previous Month", useUnmergedTree = true)
            .performClick()
        if (currentMonthCapitalized == "January"){
            currentYear--
        }

        composeTestRule.onNodeWithText("$previousMonthCapitalized $currentYear", useUnmergedTree = true).assertIsDisplayed()
        if (previousMonthCapitalized == "December"){
            currentYear++
        }

        //next month
        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("$currentMonthCapitalized $currentYear", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun monthOverviewNumberOfDaysTest() {

        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue
        val yearMonth = YearMonth.of(currentYear, currentMonth)
        val numberOfDays:String = yearMonth.lengthOfMonth().toString()

        //number of days
        composeTestRule.onNodeWithText(numberOfDays, useUnmergedTree = true).assertIsDisplayed()

        //number of days next month
        val nextMonthYearMonth = YearMonth.from(currentDate.plusMonths(1))
        val numberOfDaysInNextMonth = nextMonthYearMonth.lengthOfMonth().toString()
        composeTestRule.onNodeWithTag("Next Month", useUnmergedTree = true)
            .performClick()
        composeTestRule.onNodeWithText(numberOfDaysInNextMonth, useUnmergedTree = true).assertIsDisplayed()

    }


}
