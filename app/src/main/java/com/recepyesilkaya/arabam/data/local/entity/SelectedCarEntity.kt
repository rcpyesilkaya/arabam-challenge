package com.recepyesilkaya.arabam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_selected")
data class SelectedCarEntity(
    @PrimaryKey @ColumnInfo(name = "selected_car_id") var selected_car_id: Int,
)