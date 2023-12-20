import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.domain.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.presentation.viewmodel.UtilityHelper
import com.google.android.gms.location.LocationServices
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.Month


@RunWith(AndroidJUnit4::class)
class AppViewModelTest {


    @get:Rule
    private lateinit var viewModel: AppViewmodel
    private lateinit var eventRepository: EventRepository
    private lateinit var holidayRepository: HolidayRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val mockApplication = ApplicationProvider.getApplicationContext<Application>()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val utilityHelper = UtilityHelper(context)

        // Create the repositories (mock or real, depending on your needs)
        val eventDb = EventRoomDatabase.getInstance(mockApplication)
        val eventDao = eventDb.eventDao()
        val eventRepository = EventRepository(eventDao)
        val holidayRepository = MockHolidayRepository(utilityHelper)

        // Initialize the ViewModel
        viewModel = AppViewmodel(mockApplication, utilityHelper, fusedLocationClient)
        viewModel.roomRepository = eventRepository
        viewModel.holidayRepository = holidayRepository
    }

    @Test
    fun insertEvent() {
        val event = Event(
            LocalDateTime.now(),
            "Test Event",
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Description",
            "Client",
            "Location"
        )
        viewModel.insertEvent(event)
        assertTrue(viewModel.allEvents.value?.get(0) == event)
    }

    @Test
    fun deleteEvent() {
        val eventName = "Test Event"
        val result = viewModel.deleteEvent(0)
        viewModel.allEvents.value?.isEmpty()?.let { assertTrue(it) }
    }

    @Test
    fun findEventsById() {
        val eventName = "Test Event"
        val events = listOf(
            Event(
                LocalDateTime.now(),
                eventName,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Description",
                "Clients",
                "Location"
            )
        )
        viewModel.findEventsById(0)
        val result = viewModel.searchResults.value?.get(0)
        assertEquals(events[0], result)
    }

//    @Test
//    fun findEventsByDay() {
//        val day = LocalDateTime.now()
//        val events = listOf(
//            Event(
//                day,
//                "Test Event",
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                "Description",
//                "Clients",
//                "Location"
//            )
//        )
//
////        viewModel.searchResults.value = events
//        viewModel.findEventsByDay(day)
//        val result = viewModel.searchResults.value
//
//        assertEquals(events[0], result?.get(0))
//    }
//
//    @Test
//    fun checkConflictingEvents_noConflict() {
//        val start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
//        val end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 13, 0)
//
//        // assuming there are no events in the viewModel.searchResults
//        viewModel.searchResults.value = emptyList()
//
//        val result = viewModel.checkConflictingEvents(2, start, end)
//
//        // should be no conflict, so the result should be null
//        assertEquals(null, result)
//    }
//
//    @Test
//    fun checkConflictingEvents_conflict() {
//        val viewModel = AppViewmodel()
//
//        // Create an event that conflicts with the given time range
//        val conflictingEvent = Event(
//            LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 30),
//            "Conflicting Event",
//            LocalDateTime.of(2023, Month.NOVEMBER, 1, 13, 30),
//            LocalDateTime.of(2023, Month.NOVEMBER, 1, 14, 0),
//            "Description",
//            "Client",
//            "Location"
//        )
//
//        viewModel.searchResults.value = listOf(conflictingEvent)
//
//        val start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
//        val end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 13, 0)
//
//        val result = viewModel.checkConflictingEvents(start, end)
//
//        assertEquals("Overlaps another event: Check time values", result)
//    }
//
//    @Test
//    fun checkConflictingEvents_sameStartAndEnd() {
//        val viewModel = AppViewmodel()
//
//        val start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
//        val end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
//
//        viewModel.searchResults.value = emptyList()
//
//        val result = viewModel.checkConflictingEvents(start, end)
//
//        // Start and end times are the same, conflict
//        assertEquals("Start and End times are the same", result)
//    }
//
//
//
    class MockHolidayRepository(utilityHelper:UtilityHelper) : HolidayRepository(utilityHelper = utilityHelper) {
        private val holidays = MutableStateFlow<List<Holiday>>(emptyList())

        override suspend fun getHolidays(): List<Holiday> {
            return holidays.value
        }
    }
}
