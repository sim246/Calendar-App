package com.example.calendarapp.ui.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EventRepository (private val eventDao: EventDao) {

    val searchResults = MutableLiveData<List<Event>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProduct(newevent: Event) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.insertEvent(newevent)
        }
    }

    fun deleteProduct(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.deleteEvents(name)
        }
    }

    fun findProduct(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findEvents(name)
        }
}




