import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.data.db.EventDao
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.data.retrofit.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.setMain
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


    class MockHolidayRepository : HolidayRepository() {
        private val holidays = MutableStateFlow<List<Holiday>>(emptyList())

        override suspend fun getHolidays(): List<Holiday> {
            return holidays.value
        }

    }

}