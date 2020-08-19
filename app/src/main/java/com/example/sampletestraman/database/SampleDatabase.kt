package com.example.sampletestraman.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [
            Person::class
        ],
        exportSchema = false,
        version = 1

)


abstract class SampleDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SampleDatabase? = null

        fun getDatabase(context: Context): SampleDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        SampleDatabase::class.java,
                        "safe_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}