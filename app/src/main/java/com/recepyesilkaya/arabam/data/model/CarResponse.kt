package com.recepyesilkaya.arabam.data.model

data class CarResponse(
    val id: Long?,
    val title: String?,
    val location: CarLocation?,
    val category: Category?,
    val modelName: String?,
    val price: Int?,
    val priceFormatted: String?,
    val date: String?,
    val dateFormatted: String?,
    val photo: String?,
    val properties: List<Property>?
)