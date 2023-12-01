package com.example.calendarapp.ui.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.ui.data.db.EventRepository
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.data.retrofit.Holiday
import com.example.calendarapp.ui.data.retrofit.HolidayRepository
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

class AppViewmodel(application: Application) : ViewModel(){

    val allEvents: LiveData<List<Event>>
    private val eventRepository: EventRepository
    val searchResults: MutableLiveData<List<Event>>
    var currentDay:Date = Date()

    init {
        val productDb = EventRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        eventRepository = EventRepository(productDao)

        allEvents = eventRepository.allEvents
        searchResults = eventRepository.searchResults
    }


    private val holidayRepository = HolidayRepository()
    private val _holidays = MutableLiveData<List<Holiday>>()
    val holidays: LiveData<List<Holiday>> = _holidays



    fun insertEvent(event: Event): Boolean {
        return try {
            eventRepository.insertEvent(event)
            true
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            false
        }
    }

    fun findEventByName(name: String) {
        eventRepository.findEventsByName(name)
    }

    fun findEventByDay(day: Date) {
        eventRepository.findEventsByDay(day)
    }


    fun deleteProduct(name: String) {
        eventRepository.deleteEvent(name)
    }

    fun fetchHolidays() {
        viewModelScope.launch {
            try {
                val hol = holidayRepository.getHolidays()
                _holidays.value = hol
                Log.e("FetchHoliday", _holidays.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("FetchHoliday", e.message.toString())
            }
        }
    }

    fun getDaysWithEvents(month: YearMonth): List<Date>? {
        val events: List<Event>? = allEvents.value
        return if (events != null) {
            val monthsEvents = events.filter {

                val c: Calendar = Calendar.getInstance()
                c.time = it.day
                val theDay: Int = c.get(Calendar.DAY_OF_WEEK)
                val theMonth: Int = c.get(Calendar.MONTH)
                val eventMonth = YearMonth.of(theDay, theMonth)
                eventMonth == month
            }
            val eventDates = monthsEvents.map { it.start }.toSet()
            eventDates.toList()
        } else {
            null
        }
    }

    fun setNewDay(d:Date){
        currentDay = d
    }
}