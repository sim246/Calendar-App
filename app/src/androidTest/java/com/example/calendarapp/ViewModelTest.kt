import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.data.retrofit.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
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

//        val mockContext = ApplicationProvider.getApplicationContext<Context>()
        val mockApplication = ApplicationProvider.getApplicationContext<Application>()

        viewModel.roomRepository = eventRepository

        //have to initilize this..
        //eventRepository = MockEventRepository()
        holidayRepository = MockHolidayRepository()

        viewModel = AppViewmodel(mockApplication)
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
        val result = viewModel.insertEvent(event)
        assertTrue(result)
    }

    @Test
    fun deleteEvent() {
        val eventName = "Test Event"
        val result = viewModel.deleteEvent(eventName)
        assertTrue(result)
    }

    @Test
    fun findEventsByName() {
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
        viewModel.searchResults.value = events
        val result = viewModel.findEventsByName(eventName)
        assertEquals(events, result)
    }

    @Test
    fun findEventsByDay() {
        val day = LocalDateTime.now()
        val events = listOf(
            Event(
                day,
                "Test Event",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Description",
                "Clients",
                "Location"
            )
        )

//        viewModel.searchResults.value = events
        viewModel.findEventsByDay(day)
        val result = viewModel.searchResults.value

        assertEquals(events[0], result?.get(0))
    }

    @Test
    fun checkConflictingEvents_noConflict() {
        val viewModel = AppViewmodel()

        val start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
        val end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 13, 0)

        // assuming there are no events in the viewModel.searchResults
        viewModel.searchResults.value = emptyList()

        val result = viewModel.checkConflictingEvents(start, end)

        // should be no conflict, so the result should be null
        assertEquals(null, result)
    }

    @Test
    fun checkConflictingEvents_conflict() {
        val viewModel = AppViewmodel()

        // Create an event that conflicts with the given time range
        val conflictingEvent = Event(
            LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 30),
            "Conflicting Event",
            LocalDateTime.of(2023, Month.NOVEMBER, 1, 13, 30),
            LocalDateTime.of(2023, Month.NOVEMBER, 1, 14, 0),
            "Description",
            "Client",
            "Location"
        )

        viewModel.searchResults.value = listOf(conflictingEvent)

        val start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
        val end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 13, 0)

        val result = viewModel.checkConflictingEvents(start, end)

        assertEquals("Overlaps another event: Check time values", result)
    }

    @Test
    fun checkConflictingEvents_sameStartAndEnd() {
        val viewModel = AppViewmodel()

        val start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
        val end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)

        viewModel.searchResults.value = emptyList()

        val result = viewModel.checkConflictingEvents(start, end)

        // Start and end times are the same, conflict
        assertEquals("Start and End times are the same", result)
    }



    class MockHolidayRepository : HolidayRepository() {
        private val holidays = MutableStateFlow<List<Holiday>>(emptyList())

        override suspend fun getHolidays(): List<Holiday> {
            return holidays.value
        }
    }
}