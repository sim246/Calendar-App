package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.calendarapp.ui.domain.Event
import java.time.LocalDateTime

@Dao
interface EventDao {
    @Insert
    fun insertEvent(event: Event)

//    @Query("SELECT * FROM events WHERE eventName = :name")
    @Query("SELECT * FROM events_db WHERE eventName = :name")
    fun findEvents(name: String): List<Event>

//    @Query("DELETE FROM events WHERE eventName = :name")
    @Query("DELETE FROM events_db WHERE eventName = :name")
    fun deleteEvents(name: String)

//    @Query("SELECT * FROM events")
    @Query("SELECT * FROM events_db")
    fun getAllEvents(): LiveData<List<Event>>

//    @Query("SELECT * FROM events WHERE day = :day")
    @Query("SELECT * FROM events_db WHERE day = :day")
    fun findAllEventsByDay(day: LocalDateTime): List<Event>

}