package com.example.calendarapp.ui.domain

import java.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
class Event{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int = 0

    @ColumnInfo(name = "day")
    var day: Date = Calendar.getInstance().time

    @ColumnInfo(name = "eventName")
    var eventName: String = ""

    @ColumnInfo(name = "start")
    var start: Date = day

    @ColumnInfo(name = "theEnd")
    var theEnd: Date = day

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "clientName")
    var clientName: String = ""

    @ColumnInfo(name = "location")
    var location: String = ""

//    constructor()

    constructor(day:Date, eventName: String, start: Date, theEnd: Date, description: String, clientName: String, location: String) {
        this.day = day
        this.eventName = eventName
        this.start = start
        this.theEnd = theEnd
        this.description = description
        this.clientName = clientName
    }
}