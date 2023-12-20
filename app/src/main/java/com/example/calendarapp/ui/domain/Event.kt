package com.example.calendarapp.ui.domain

import java.time.LocalDateTime
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_db")
class Event(
    @ColumnInfo(name = "day") var day: LocalDateTime,
    @ColumnInfo(name = "eventName") var eventName: String,
    @ColumnInfo(name = "start") var start: LocalDateTime,
    @ColumnInfo(name = "theEnd") var theEnd: LocalDateTime,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "clientName") var clientName: String?,
    @ColumnInfo(name = "location") var location: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0
}