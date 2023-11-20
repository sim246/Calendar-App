package com.example.calendarapp.ui.resources

import retrofit2.http.GET
interface HolidayService {
    @GET("holidays")
    suspend fun getHolidays(): Holiday
}
