package com.recepyesilkaya.arabam.data.network


import com.recepyesilkaya.arabam.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        @Volatile
        private var INSTANCERx: Retrofit? = null

        fun getRetrofit(): Retrofit {
            synchronized(this) {
                INSTANCERx?.let {
                    return it
                }
                INSTANCERx =
                    Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                return INSTANCERx as Retrofit
            }
        }
    }
}