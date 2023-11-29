package com.example.calendarapp.ui.data.db
import com.example.calendarapp.ui.domain.Event
import kotlinx.coroutines.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [(Event::class)], version = 1)
@TypeConverters(Converters::class)
abstract class EventRoomDatabase: RoomDatabase() {

    abstract fun productDao(): EventDao
    companion object {
        private var INSTANCE: EventRoomDatabase? = null

        fun getInstance(context: Context): EventRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventRoomDatabase::class.java,
                        "event_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
