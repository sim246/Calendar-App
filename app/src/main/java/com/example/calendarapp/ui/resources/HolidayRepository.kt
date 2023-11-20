package com.example.calendarapp.ui.resources

class HolidayRepository {
    private val holidayService = RetrofitInstance.holidayService

    suspend fun getCreditCards(): Holiday {
        return holidayService.getHolidays()
    }
}