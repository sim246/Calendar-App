package com.example.calendarapp.ui.data.db

import androidx.annotation.WorkerThread
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
    suspend fun insertEvent(newEvent: Event) = eventDao.insertEvent(newEvent)
    suspend fun deleteEvent(name: String) = eventDao.deleteEvents(name)

    @WorkerThread
    suspend fun findEvent(name: String) = eventDao.findEvents(name)
    suspend fun findEventByDay(day: LocalDateTime) = eventDao.findAllEventsByDay(day)

    fun getAllEvents():LiveData<List<Event>> = eventDao.getAllEvents()
}