package com.example.calendarapp.ui.data.retrofit

import android.util.Log
import com.example.calendarapp.ui.domain.Holiday
import com.example.calendarapp.ui.presentation.viewmodel.UtilityHelper
import java.time.LocalDateTime


open class HolidayRepository(private val utilityHelper: UtilityHelper) {
    private val holidayService = RetrofitInstance.holidayService
    open suspend fun getHolidays(): List<Holiday> {
        val holidays : MutableList<Holiday> = mutableListOf()
        Log.d("day", utilityHelper.locale)
        val date: LocalDateTime = LocalDateTime.now()

        holidays.addAll(holidayService.getHolidays(date.year.toString(),utilityHelper.locale))
        holidays.addAll(holidayService.getHolidays(date.minusYears(1).year.toString(),utilityHelper.locale))
        holidays.addAll(holidayService.getHolidays(date.plusYears(1).year.toString(),utilityHelper.locale))
//        holidays.addAll(holidayService.getHolidays("2023","CA"))
//        holidays.addAll(holidayService.getHolidays("2022","CA"))
//        holidays.addAll(holidayService.getHolidays("2024","CA"))
        return holidays
    }
}