package com.recepyesilkaya.arabam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_favorite")
data class FavoriteEntity(
    @PrimaryKey @ColumnInfo(name = "car_id") val car_id: Long,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "city_name") val cityName: String?,
    @ColumnInfo(name = "town_name") val townName: String?,
    @ColumnInfo(name = "category_id") val categoryId: Long?,
    @ColumnInfo(name = "category_name") val categoryName: String?,
    @ColumnInfo(name = "model_name") val modelName: String?,
    @ColumnInfo(name = "price") val price: Int?,
    @ColumnInfo(name = "price_formatted") val priceFormatted: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "date_formatted") val dateFormatted: String?,
    @ColumnInfo(name = "photo") val photo: String?
)