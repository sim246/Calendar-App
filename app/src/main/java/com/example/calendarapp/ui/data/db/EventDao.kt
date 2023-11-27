package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calendarapp.ui.domain.Event
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface EventDao {
    @Insert
    fun insertEvent(event: Event)

    @Query("SELECT * FROM events WHERE eventName = :name")
    fun findEvents(name: String): List<Event>

    @Query("DELETE FROM events WHERE eventName = :name")
    fun deleteEvents(name: String)

    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("INSERT INTO events (day, eventName, start,theEnd, description, clientName, location) VALUES (:day, :eventName, :start, :end, :description, :clientName, :location)")
    fun addEvent(day: LocalDate, eventName: String, start: LocalDateTime, end: LocalDateTime, description: String, clientName: String, location: String)

    @Query("SELECT * FROM events WHERE day = :day")
    fun getAllEventsByDay(day: LocalDate): LiveData<List<Event>>
}