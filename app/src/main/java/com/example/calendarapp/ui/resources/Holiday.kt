package com.example.calendarapp.ui.resources

import java.time.LocalDate

data class Holiday(
    val date: LocalDate,
    val localName: String,
    val name: String,
    val countryCode: String,

    val fixed: Boolean,
    val global: Boolean,
    val counties : String?,
    val launchYear: Int?,
    val types : List<String>
)