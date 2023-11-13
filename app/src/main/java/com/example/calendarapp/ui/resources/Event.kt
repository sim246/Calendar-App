package com.example.calendarapp.ui.resources

import java.time.LocalDateTime

class Event(
    val eventName: String,
    val start: LocalDateTime,
    val end: LocalDateTime,

    val description: String,
    val clientName: String,
    val location: String
)