package com.example.calendarapp

sealed class Routes (val route: String) {
    object MonthOverviewScreen : Routes("month")
    object DailyOverview : Routes("day")
    object EventOverview : Routes("event")
    //View for a singular event, for editing / creating
    object EventEdit : Routes("eventEdit")
}