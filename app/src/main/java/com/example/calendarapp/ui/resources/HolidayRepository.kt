package com.example.calendarapp.ui.resources

class HolidayRepository {
    private val holidayService = RetrofitInstance.holidayService

    suspend fun getHolidays(): List<Holiday> {
        return holidayService.getHolidays()
    }
}