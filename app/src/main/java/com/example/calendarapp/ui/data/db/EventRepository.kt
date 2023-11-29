package com.example.calendarapp.ui.data.db

import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventRepository(private val eventDao: EventDao) {

    private val searchResults = MutableLiveData<List<Event>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertEvent(newevent: Event) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.insertEvent(newevent)
        }
    }

    fun deleteEvent(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.deleteEvents(name)
        }
    }

    fun findEvent(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            this@EventRepository.searchResults.value = findEventAsync(name).await()
        }
    }

    private fun findEventAsync(name: String): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findEvents(name)
        }

    fun findEventByDay(day: LocalDate) {
        coroutineScope.launch(Dispatchers.Main) {
            this@EventRepository.searchResults.value = findDayEventsAsync(day).await()
        }
    }

    private fun findDayEventsAsync(day: LocalDate): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findAllEventsByDay(day)
        }
}