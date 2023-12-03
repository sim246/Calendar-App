package com.example.calendarapp.ui.data.retrofit

open class HolidayRepository {
    private val holidayService = RetrofitInstance.holidayService

    open suspend fun getHolidays(): List<Holiday> {
        val holidays : MutableList<Holiday> = mutableListOf()
        holidays.addAll(holidayService.getHolidays1())
        holidays.addAll(holidayService.getHolidays2())
        return holidays
    }
}