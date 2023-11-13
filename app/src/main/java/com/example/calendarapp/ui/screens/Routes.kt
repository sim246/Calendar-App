package com.example.calendarapp.ui.screens


sealed class Routes(val route:String) {
    object MonthOverview : Routes("month")
    object DayOverview : Routes("day")
    object EventOverview : Routes("event")


}

