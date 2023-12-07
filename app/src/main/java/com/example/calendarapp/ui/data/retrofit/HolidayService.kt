package com.example.calendarapp.ui.data.retrofit

import com.example.calendarapp.ui.domain.Holiday
import retrofit2.http.GET
import retrofit2.http.Path

//var code:String = "CA"
interface HolidayService {
    @GET("{year}/{code}")
    suspend fun getHolidays1(
        @Path("year") year : String,
        @Path("code") code : String,
    ): List<Holiday>
//    @GET("2024/CA")
//    suspend fun getHolidays2(code: String): List<Holiday>
}
