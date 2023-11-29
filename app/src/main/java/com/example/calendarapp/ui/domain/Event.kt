package com.example.calendarapp.ui.domain

import java.time.LocalDate
import java.time.LocalDateTime
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class Event{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "productId")
    var id: Int = 0

    @ColumnInfo(name = "day")
    var day: LocalDate = LocalDate.parse("2023-11-18")

    @ColumnInfo(name = "eventName")
    var eventName: String = ""

    @ColumnInfo(name = "start")
    var start: LocalDateTime = LocalDateTime.parse("2023-11-18T06:15:00")

    @ColumnInfo(name = "theEnd")
    var theEnd: LocalDateTime = LocalDateTime.parse("2023-11-18T07:15:00")

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "clientName")
    var clientName: String = ""

    @ColumnInfo(name = "location")
    var location: String = ""

    constructor()

    constructor(day: LocalDate, eventName: String, start: LocalDateTime, theEnd: LocalDateTime, description: String, clientName: String, location: String) {
        this.day = day
        this.eventName = eventName
        this.start = start
        this.theEnd = theEnd
        this.description = description
        this.clientName = clientName
    }
}