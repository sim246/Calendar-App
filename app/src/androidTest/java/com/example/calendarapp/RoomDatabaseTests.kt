package com.example.calendarapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.ui.data.db.EventDao
import com.example.calendarapp.ui.data.db.EventRoomDatabase
import com.example.calendarapp.ui.domain.Event
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.DefaultTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTests {

    private lateinit var eventDao: EventDao
    private lateinit var db: EventRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, EventRoomDatabase::class.java)
            .allowMainThreadQueries() // For simplicity in testing, should not be used in production
            .build()
        eventDao = db.eventDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAndGetEvent() = runBlockingTest {
        ArchTaskExecutor.getInstance().setDelegate(DefaultTaskExecutor())
        val currentDateTime = LocalDateTime.now()
        val event = Event(
            day = currentDateTime,
            eventName = "name",
            start = currentDateTime,
            theEnd = currentDateTime.plusHours(2),
            description = "des",
            clientName = "John Doe",
            location = "loc"
        )
        eventDao.insertEvent(event)
        val allEvents = LiveDataTestUtil.getValue(eventDao.getAllEvents())

        assertThat(allEvents?.size ?: 0, `is`(1))
        assertThat(allEvents?.get(0)?.eventName, `is`("name"))
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

object LiveDataTestUtil {
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>, time: Long = 2, timeUnit: TimeUnit = TimeUnit.SECONDS): T? {
        var data: T? = null
        val latch = CountDownLatch(1)

        val observer = { t: T? ->
            data = t
            latch.countDown()
        }
        liveData.observeForever(observer)
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value not set within the specified timeout")
        }
        liveData.removeObserver(observer)
        return data
    }
}