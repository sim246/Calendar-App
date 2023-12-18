package com.example.calendarapp.ui.presentation.viewmodel

import android.content.Context

class UtilityHelper(context: Context) {
    val locale: String = context.resources.configuration.locales[0].country
}
