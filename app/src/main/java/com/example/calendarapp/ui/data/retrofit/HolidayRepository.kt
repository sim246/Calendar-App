package com.example.calendarapp.ui.data.retrofit

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.calendarapp.ui.domain.Holiday


open class HolidayRepository(context: Context) {
    private val holidayService = RetrofitInstance.holidayService
    private val context: Context = context

    open suspend fun getHolidays(): List<Holiday> {
        val holidays : MutableList<Holiday> = mutableListOf()

        val locale: String = context.resources.configuration.locales[0].country
        Log.d("day", locale)

        holidays.addAll(holidayService.getHolidays1("2023","CA"))
//        holidays.addAll(holidayService.getHolidays2())
        return holidays
    }
}