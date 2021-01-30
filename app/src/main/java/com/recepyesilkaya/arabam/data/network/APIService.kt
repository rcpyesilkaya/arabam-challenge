package com.recepyesilkaya.arabam.data.network

import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.model.CarResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/api/v1/listing")
    fun getData(
        @Query("sort") sort: Int,
        @Query("sortDirection") sortDirection: Int,
        @Query("take") take: Int
    ): Single<List<CarResponse>>

    @GET("/api/v1/detail")
    fun getDetail(@Query("id") id: Long): Single<CarDetail?>

}