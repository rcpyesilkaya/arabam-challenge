package com.recepyesilkaya.arabam.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.recepyesilkaya.arabam.data.local.dao.CarEntityDAO
import com.recepyesilkaya.arabam.data.local.entity.CarEntity


@Database(
    entities = [CarEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CarRoomDatabase : RoomDatabase() {

    abstract fun carEntityDAO(): CarEntityDAO

    companion object {

        @Volatile
        private var INSTANCE: CarRoomDatabase? = null

        fun getDatabase(context: Context): CarRoomDatabase {
            val tempInstance = INSTANCE

            tempInstance?.let {
                return it
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarRoomDatabase::class.java,
                    "car_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}