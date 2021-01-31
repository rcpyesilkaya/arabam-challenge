package com.recepyesilkaya.arabam.data.model

data class CarDetail(
    val id: Long?,
    val title: String?,
    val location: CarLocation?,
    val category: Category?,
    val modelName: String?,
    val price: Int?,
    val priceFormatted: String?,
    val date: String?,
    val dateFormatted: String?,
    val photos: List<String>?,
    val properties: List<Property>?,
    val text: String?,
    val userInfo: User?
)