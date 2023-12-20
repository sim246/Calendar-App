package com.example.calendarapp

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.domain.Event
import com.example.calendarapp.ui.domain.Holiday
import com.example.calendarapp.ui.data.networking.retrofit.HolidayRepository
import com.example.calendarapp.ui.presentation.viewmodel.AppViewmodel
import com.example.calendarapp.ui.presentation.viewmodel.UtilityHelper
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.Month


@RunWith(AndroidJUnit4::class)
class AppViewModelTest {

    private lateinit var viewModel: AppViewmodel

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
    fun checkConflictingEvents_noConflict() {
        val start = LocalDateTime.of(2023, Month.DECEMBER, 25, 13, 0)
        val end = LocalDateTime.of(2023, Month.DECEMBER, 25, 14, 0)

        val eventsList: List<Event> = listOf(
            Event(
                LocalDateTime.of(2023, 12, 25, 0, 0),
                "name",
                LocalDateTime.of(2023, 12, 25, 8, 0),
                LocalDateTime.of(2023, 12, 25, 9, 0),
                "des",
                "John Doe",
                "loc"
            ),
            Event(
                LocalDateTime.of(2023, 12, 26, 0, 0),
                "name",
                LocalDateTime.of(2023, 12, 26, 13, 0),
                LocalDateTime.of(2023, 12, 26, 14, 0),
                "des",
                "John Doe",
                "loc"
            )
        )
        val result = viewModel.checkConflictingEvents(2, start, end, eventsList)
        assertEquals(null, result)
    }

    @Test
    fun checkConflictingEvents_conflict() {
        val start = LocalDateTime.of(2023, 11, 1, 13, 0)
        val end = LocalDateTime.of(2023, 11, 1, 15, 0)

        // Create an event that conflicts with the given time range
        var conflictingEvent = Event(
            LocalDateTime.of(2023, Month.NOVEMBER, 1, 0, 0),
            "Conflicting Event",
            start,
            end,
            "Description",
            "Client",
            "Location"
        )

        val eventsList: List<Event> = listOf(
            Event(
                LocalDateTime.of(2023, 12, 25, 0, 0),
                "name",
                LocalDateTime.of(2023, 12, 25, 8, 0),
                LocalDateTime.of(2023, 12, 25, 9, 0),
                "des",
                "John Doe",
                "loc"
            ),
            conflictingEvent
        )

        var result = viewModel.checkConflictingEvents(3, start, end, eventsList)
        assertEquals("Start time overlaps another event, check time values", result)

        conflictingEvent = Event(
            LocalDateTime.of(2023, 11, 1, 0, 0),
            "Conflicting Event",
            LocalDateTime.of(2023, 11, 1, 14, 0),
            end,
            "Description",
            "Client",
            "Location"
        )

        val eventsList2: List<Event> = listOf(
            Event(
                LocalDateTime.of(2023, 12, 26, 0, 0),
                "name",
                LocalDateTime.of(2023, 12, 26, 13, 0),
                LocalDateTime.of(2023, 12, 26, 14, 0),
                "des",
                "John Doe",
                "loc"
            ),
            conflictingEvent
        )
        result = viewModel.checkConflictingEvents(3, start, end, eventsList2)
        assertEquals("End time overlaps another event, check time values", result)
    }

    @Test
    fun checkConflictingEvents_sameStartAndEnd() {
        val eventsList: List<Event> = listOf()

        var start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
        var end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 12, 0)
        var result = viewModel.checkConflictingEvents(1, start, end, eventsList)
        assertEquals("Start and End times are the same", result)

        start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 9, 0)
        end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 7, 0)
        result = viewModel.checkConflictingEvents(1, start, end, eventsList)
        assertEquals("Start time must be before the end time", result)

        start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 1, 0)
        end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 2, 0)
        result = viewModel.checkConflictingEvents(1, start, end, eventsList)
        assertEquals("events must be between 6 am and 12 pm", result)
    }


    @Test
    fun testTheSets() {
        val date = LocalDateTime.now()
        viewModel.setNewDay(date)
        assertEquals(viewModel.currentDay, date)

        val event = Event(
            LocalDateTime.of(2023, 12, 26, 0, 0),
            "name",
            LocalDateTime.of(2023, 12, 26, 13, 0),
            LocalDateTime.of(2023, 12, 26, 14, 0),
            "des",
            "John Doe",
            "loc"
        )
        viewModel.setCurrentEvent(event)
        assertEquals(viewModel.currentlyViewingEvent, event)
    }


    class MockHolidayRepository(utilityHelper:UtilityHelper) : HolidayRepository(utilityHelper = utilityHelper) {
        private val holidays = MutableStateFlow<List<Holiday>>(emptyList())

        override suspend fun getHolidays(): List<Holiday> {
            return holidays.value
        }
    }
}
