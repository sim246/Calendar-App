package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.ui.domain.Event

class EventRepository (private val eventDao: EventDao) {
    val allProducts: LiveData<List<Event>> = eventDao.getAllProducts()
    val searchResults = MutableLiveData<List<Event>>()

}