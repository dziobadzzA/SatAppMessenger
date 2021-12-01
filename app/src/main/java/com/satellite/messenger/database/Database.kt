package com.satellite.messenger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.satellite.messenger.database.entity. *

@Database(entities = [Auth::class, Messages::class, Profile::class], version = 1, exportSchema = false)
abstract  class Database: RoomDatabase() {

    abstract val databaseDao: DatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: com.satellite.messenger.database.Database? = null

        fun getInstance(context: Context): com.satellite.messenger.database.Database {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        com.satellite.messenger.database.Database::class.java,
                        "Sat"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }
}