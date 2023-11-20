package com.example.calendarapp.ui.resources

import retrofit2.http.GET
interface HolidayService {
    @GET("2023/CA")
    suspend fun getHolidays1(): List<Holiday>
    @GET("2024/CA")
    suspend fun getHolidays2(): List<Holiday>
}
