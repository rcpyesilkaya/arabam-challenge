package com.recepyesilkaya.arabam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import io.reactivex.Completable

@Dao
interface SelectedCarDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSelectCar(selectedCarEntity: SelectedCarEntity): Completable

    @Query("SELECT * FROM entity_selected")
    suspend fun getAllSelectedCars(): List<SelectedCarEntity>
}