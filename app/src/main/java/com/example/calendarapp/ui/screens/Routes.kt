package com.example.calendarapp.ui.screens


sealed class Routes(val route:String) {
    //Monthly calendar
    object MonthOverview : Routes("month")
    //Day for a specific month (obtained somehow from view-model)
    object DayOverview : Routes("day")
    //View for a singular event, not editing
    object EventOverview : Routes("event")
    //View for a singular event, for editing / creating
    object EventEdit : Routes("eventEdit")


}

