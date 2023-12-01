package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()
    val searchResults = MutableLiveData<List<Event>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertEvent(newEvent: Event) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.insertEvent(newEvent)
        }
    }

    fun deleteEvent(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.deleteEvents(name)
        }
    }

    fun findEventsByName(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            this@EventRepository.searchResults.value = findEventsByNameAsync(name).await()
        }
    }

    private fun findEventsByNameAsync(name: String): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findEvents(name)
        }

    fun findEventsByDay(day: Date) {
        coroutineScope.launch(Dispatchers.Main) {
            this@EventRepository.searchResults.value = findEventsByDayAsync(day).await()
        }
    }

    private fun findEventsByDayAsync(day: Date): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findAllEventsByDay(day)
        }
}