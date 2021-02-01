package com.recepyesilkaya.arabam.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.recepyesilkaya.arabam.data.local.dao.CarDAO
import com.recepyesilkaya.arabam.data.local.entity.CarEntity


@Database(
    entities = [CarEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CarRoomDatabase : RoomDatabase() {
    abstract fun carDAO(): CarDAO
}