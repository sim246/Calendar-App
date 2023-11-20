package com.example.calendarapp.ui.resources

import java.time.LocalDate

data class Holiday(
    val date: LocalDate,
    val localName: String,
    val holidayName: String,

    val fixed: Boolean,
    val global: Boolean,
)