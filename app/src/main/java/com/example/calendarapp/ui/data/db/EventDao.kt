package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calendarapp.ui.domain.Event
import java.time.LocalDateTime

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: Event)
    @Query("SELECT * FROM events_db WHERE eventName = :name")
    suspend fun findEvents(name: String): List<Event>
    @Query("DELETE FROM events_db WHERE eventName = :name")
    suspend fun deleteEvents(name: String)
    @Query("SELECT * FROM events_db")
    fun getAllEvents(): LiveData<List<Event>>
    @Query("SELECT * FROM events_db WHERE day = :day ORDER BY start ASC")
    suspend fun findAllEventsByDay(day: LocalDateTime): List<Event>

}