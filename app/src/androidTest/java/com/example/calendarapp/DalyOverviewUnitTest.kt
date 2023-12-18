//package com.example.calendarapp
//
//import android.app.Application
//import android.content.Context
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import com.example.calendarapp.ui.presentation.screens.MainActivity
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.hasParent
//import androidx.compose.ui.test.hasTestTag
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onAllNodesWithTag
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.runner.AndroidJUnitRunner
//import com.example.calendarapp.ui.presentation.screens.AppViewmodelFactory
//import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
//import com.example.calendarapp.ui.theme.CalendarAppTheme
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RequiresApi(Build.VERSION_CODES.O)
//@RunWith(AndroidJUnit4::class)
//class DalyOverviewUnitTest {
//
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createComposeRule()
//
//    //Call the MySimpleUI function
//    @Before
//    fun setUP() {
//        composeTestRule.setContent {
//            CalendarAppTheme {
//                    val owner = LocalViewModelStoreOwner.current
//
//                    owner?.let {
//                        val viewModel: AppViewmodel = viewModel(
//                            it,
//                            "AppViewmodel",
//                            AppViewmodelFactory(
//                                LocalContext.current.applicationContext
//                                        as Application, LocalContext.current
//                            )
//                        )
//                        MainActivity.
//                    }
//
//
////                val viewModel = AppViewmodel()
////                val navController = rememberNavController()
////                val holidays by viewModel.holidays.observeAsState(null)
////
////                LaunchedEffect(Unit) {
////                    viewModel.fetchHolidays()
////                }
////                DailyOverview(holidays, viewModel, navController)
//            }
//        }
//    }
//
//    @Test
//    fun verifyIfAllViewsIsDisplayed() {
//        composeTestRule.onNodeWithTag("DailyOverviewUI").assertExists().assertIsDisplayed()
//
//        composeTestRule.onAllNodesWithTag("Click Event Display event 1")[0].assertExists().assertIsDisplayed()
//        composeTestRule.onAllNodesWithTag("Click Event Display event 1")[1].assertExists().assertIsDisplayed()
//        composeTestRule.onAllNodesWithTag("Click Event Display event 1")[2].assertExists().assertIsDisplayed()
//
//        composeTestRule.onAllNodesWithTag("Click Event Display event 2")[0].assertExists().assertIsDisplayed()
//        composeTestRule.onAllNodesWithTag("Click Event Display event 2")[1].assertExists().assertIsDisplayed()
//        composeTestRule.onAllNodesWithTag("Click Event Display event 2")[2].assertExists().assertIsDisplayed()
//
//        composeTestRule.onAllNodesWithTag("Click Event Display event 3")[0].assertExists().assertIsDisplayed()
//        composeTestRule.onAllNodesWithTag("Click Event Display event 3")[1].assertExists().assertIsDisplayed()
//        composeTestRule.onAllNodesWithTag("Click Event Display event 3")[2].assertExists().assertIsDisplayed()
//
//        composeTestRule.onNodeWithTag("Click Back", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("Click Add", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("Next Day", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("Previous Day", useUnmergedTree = true).assertIsDisplayed()
//    }
//
//    @Test
//    fun verifyIfHasChildrenDisplayed() {
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("DailyOverviewUI")),
//            useUnmergedTree = true
//        )[0].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 1")),
//            useUnmergedTree = true
//        )[0].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 1")),
//            useUnmergedTree = true
//        )[1].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 1")),
//            useUnmergedTree = true
//        )[2].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 2")),
//            useUnmergedTree = true
//        )[0].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 2")),
//            useUnmergedTree = true
//        )[1].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 2")),
//            useUnmergedTree = true
//        )[2].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 3")),
//            useUnmergedTree = true
//        )[0].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 3")),
//            useUnmergedTree = true
//        )[1].assertIsDisplayed()
//
//        composeTestRule.onAllNodes(
//            hasParent(hasTestTag("Click Event Display event 3")),
//            useUnmergedTree = true
//        )[2].assertIsDisplayed()
//    }
//
//
////    @Test
////    fun verifyIfHasClickAction() {
////        composeTestRule.onNodeWithTag("Click Back").performClick()
////        composeTestRule.onNodeWithTag("Click Add").performClick()
////        composeTestRule.onNodeWithTag("Next Day").performClick()
////        composeTestRule.onNodeWithTag("Previous Day").performClick()
////    }
//
//
//}
//
//class HiltTestRunner : AndroidJUnitRunner() {
//    override fun newApplication(
//        cl: ClassLoader?,
//        className: String?,
//        context: Context?,
//    ): Application {
//        return super.newApplication(cl, MainActivity::class.java.name, context)
//    }
//}