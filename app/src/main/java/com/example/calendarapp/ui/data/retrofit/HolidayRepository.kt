package com.example.calendarapp.ui.data.retrofit

import android.util.Log
import com.example.calendarapp.ui.domain.Holiday
import com.example.calendarapp.ui.presentation.viewmodel.UtilityHelper
import java.time.LocalDateTime
import java.util.Locale


open class HolidayRepository(private val utilityHelper: UtilityHelper) {
    private val holidayService = RetrofitInstance.holidayService

    open suspend fun getHolidays(): List<Holiday> {
        val holidays: MutableList<Holiday> = mutableListOf()

        val date: LocalDateTime = LocalDateTime.now()
        val languageCode = Locale.getDefault().language

        val countryCode = if (languageCode == "fr") {
            "FR" //french language, set country code to France
        } else {
            "CA"//default
        }

        Log.d("lang", languageCode)
        Log.d("ccode", countryCode)

        holidays.addAll(holidayService.getHolidays(date.year.toString(), countryCode))
        holidays.addAll(holidayService.getHolidays(date.minusYears(1).year.toString(), countryCode))
        holidays.addAll(holidayService.getHolidays(date.plusYears(1).year.toString(), countryCode))

        return holidays
    }
}
