package com.example.calendarapp.ui.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://date.nager.at/api/v3/PublicHolidays/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val holidayService: HolidayService by lazy {
        retrofit.create(HolidayService::class.java)
    }
}
