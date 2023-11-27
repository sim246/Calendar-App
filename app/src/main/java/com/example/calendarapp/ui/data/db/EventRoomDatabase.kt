package com.example.calendarapp.ui.data.db
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.*

class EventRoomDatabase(private val productDao: EventDao) {
    val searchResults = MutableLiveData<List<Event>>()

}