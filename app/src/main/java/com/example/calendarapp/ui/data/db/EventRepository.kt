package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime

open class EventRepository(private val eventDao: EventDao) {

    val allEvents : LiveData<List<Event>> = eventDao.getAllEvents()
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

    fun findEvent(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            this@EventRepository.searchResults.value = findEventAsync(name).await()
        }
    }

    private fun findEventAsync(name: String): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findEvents(name)
        }

    fun findEventByDay(day: LocalDateTime) {
        coroutineScope.launch(Dispatchers.Main) {
            this@EventRepository.searchResults.value = findDayEventsAsync(day).await()
        }
    }

    private fun findDayEventsAsync(day: LocalDateTime): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findAllEventsByDay(day)
        }
}