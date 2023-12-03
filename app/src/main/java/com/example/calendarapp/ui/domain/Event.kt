package com.example.calendarapp.ui.domain

import java.time.LocalDateTime
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.calendarapp.ui.data.db.Converters

@Entity(tableName = "events_db")
class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0

    @ColumnInfo(name = "day")
    var day: LocalDateTime = LocalDateTime.now()
    @ColumnInfo(name = "eventName")
    var eventName: String = ""
    @ColumnInfo(name = "start")
    var start: LocalDateTime = LocalDateTime.now()
    @ColumnInfo(name = "theEnd")
    var theEnd: LocalDateTime = LocalDateTime.now()

    var description: String = ""
    var clientName: String = ""
    var location: String = ""
    constructor()

    constructor(day:LocalDateTime, eventName:String, start: LocalDateTime, theEnd: LocalDateTime, description: String, clientName: String, location: String) {
        this.day = day
        this.eventName = eventName
        this.start = start
        this.theEnd = theEnd
        this.description = description
        this.clientName = clientName
        this.location = location
    }
}