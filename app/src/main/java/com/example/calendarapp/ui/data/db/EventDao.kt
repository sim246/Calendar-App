package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calendarapp.ui.domain.Event

@Dao
interface EventDao {
    @Insert
    fun insertEvent(event: Event)

    @Query("SELECT * FROM events WHERE eventName = :name")
    fun findProduct(name: String): List<Event>

    @Query("DELETE FROM events WHERE eventName = :name")
    fun deleteProduct(name: String)

    @Query("SELECT * FROM events")
    fun getAllProducts(): LiveData<List<Event>>



}