package com.example.calendarapp.ui.screens

class Routes {
    sealed class Routes(val route:String) {
        object MonthOverview : Routes("month")
        object DayOverview : Routes("day")
        object EventOverview : Routes("event")


    }

}