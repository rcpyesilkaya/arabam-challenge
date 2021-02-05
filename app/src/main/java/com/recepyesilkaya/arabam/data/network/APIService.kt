package com.recepyesilkaya.arabam.data.network

import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.model.CarResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/api/v1/listing")
    fun getData(
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("sort") sort: Int?,
        @Query("sortDirection") sortDirection: Int?,
        @Query("minYear") minYear: Int?,
        @Query("maxYear") maxYear: Int?,
        @Query("categoryId") categoryId: Int?,
        @Query("minDate") minDate: String?,
        @Query("maxDate") maxDate: String?

    ): Single<List<CarResponse>>

    @GET("/api/v1/detail")
    fun getDetail(@Query("id") id: Long): Single<CarDetail?>
}