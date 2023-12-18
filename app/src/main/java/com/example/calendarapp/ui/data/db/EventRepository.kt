package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import com.example.calendarapp.ui.domain.Event
import java.time.LocalDateTime

open class EventRepository(private val eventDao: EventDao) {
    suspend fun insertEvent(newEvent: Event) = eventDao.insertEvent(newEvent)
    suspend fun deleteEvent(name: String) = eventDao.deleteEvents(name)
    suspend fun findEvent(name: String) = eventDao.findEvents(name)
    suspend fun findEventByDay(day: LocalDateTime) = eventDao.findAllEventsByDay(day)
    fun getAllEvents():LiveData<List<Event>> = eventDao.getAllEvents()
}