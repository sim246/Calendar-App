package com.example.calendarapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.calendarapp.ui.presentation.screens.AppViewmodelFactory
import com.example.calendarapp.ui.presentation.screens.ScreenSetup
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.google.android.gms.location.LocationServices
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@RunWith(AndroidJUnit4::class)
class EventOverviewTest {
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

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
                    ScreenSetup(appViewmodel = viewModel)

                }
            }
        }
    }

    @Test
    fun verifyIfAllViewsIsDisplayedClickBack() {

        val date = LocalDateTime.now().dayOfMonth

        composeTestRule.onNodeWithText(date.toString(), useUnmergedTree = true)
            .performClick()
        composeTestRule.onNodeWithTag("Click Add", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithTag("EventEditOverviewUI").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("Save", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("No Save", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("Start", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("End", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithTag("No Save", useUnmergedTree = true)
            .performClick()
    }
}