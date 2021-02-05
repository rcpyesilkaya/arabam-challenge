package com.recepyesilkaya.arabam.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recepyesilkaya.arabam.data.local.entity.CarEntity
import io.reactivex.Completable

@Dao
interface CarDAO {
    @Query("SELECT * FROM entity_car")
    fun getAllCars(): LiveData<List<CarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(carEntity: CarEntity): Completable

    @Query("DELETE FROM entity_car")
    fun deleteAll()
}