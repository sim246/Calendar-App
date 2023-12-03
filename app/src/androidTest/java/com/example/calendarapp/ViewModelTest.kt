import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.data.db.EventDao
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

        //eventRepository = EventRepository(MockEventDao())
        holidayRepository = MockHolidayRepository()
        viewModel = AppViewmodel()
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

        viewModel.searchResults.value = events
        val result = viewModel.findEventsByDay(day)
        assertEquals(events, result)
    }




    class MockHolidayRepository : HolidayRepository() {
        private val holidays = MutableStateFlow<List<Holiday>>(emptyList())

        override suspend fun getHolidays(): List<Holiday> {
            return holidays.value
        }

    }

}