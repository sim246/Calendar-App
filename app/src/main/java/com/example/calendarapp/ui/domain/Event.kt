package com.example.calendarapp.ui.domain

import java.time.LocalDate
import java.time.LocalDateTime

class Event(
    val day: LocalDate,

    var eventName: String = "",
    var start: LocalDateTime,
    var end: LocalDateTime,

    var description: String = "",
    var clientName: String = "",
    var location: String = ""
)