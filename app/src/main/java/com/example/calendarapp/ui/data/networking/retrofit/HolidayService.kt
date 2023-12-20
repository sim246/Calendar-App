package com.example.calendarapp.ui.data.networking.retrofit

import com.example.calendarapp.ui.domain.Holiday
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayService {
    @GET("{year}/{code}")
    suspend fun getHolidays(
        @Path("year") year : String,
        @Path("code") code : String,
    ): List<Holiday>
}
