package com.example.calendarapp

sealed class Routes (val route: String) {
    object MonthOverviewScreen : Routes("month")
    object DailyOverview : Routes("day")
}
