package com.recepyesilkaya.arabam.module

import android.content.Context
import androidx.room.Room
import com.recepyesilkaya.arabam.data.local.dao.CarDAO
import com.recepyesilkaya.arabam.data.local.dao.FavoriteDAO
import com.recepyesilkaya.arabam.data.local.dao.SelectedCarDAO
import com.recepyesilkaya.arabam.data.local.database.CarRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun carDatabase(@ApplicationContext context: Context): CarRoomDatabase =
        Room.databaseBuilder(context, CarRoomDatabase::class.java, "car_database.db").build()

    @Provides
    @Singleton
    fun carDAO(cryptoDatabase: CarRoomDatabase): CarDAO =
        cryptoDatabase.carDAO()

    @Provides
    @Singleton
    fun selectedCarDAO(cryptoDatabase: CarRoomDatabase): SelectedCarDAO =
        cryptoDatabase.selectedCarDAO()

    @Provides
    @Singleton
    fun favoriteDAO(cryptoDatabase: CarRoomDatabase): FavoriteDAO =
        cryptoDatabase.favoriteDAO()
}