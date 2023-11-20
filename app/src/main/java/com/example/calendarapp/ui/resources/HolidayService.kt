package com.example.calendarapp.ui.resources

import retrofit2.http.GET
interface HolidayService {
    @GET("2023/CA")
    suspend fun getHolidays(): List<Holiday>
}
