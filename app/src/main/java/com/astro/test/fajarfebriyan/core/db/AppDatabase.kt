package com.astro.test.fajarfebriyan.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.astro.test.fajarfebriyan.data.model.Favorite
import com.astro.test.fajarfebriyan.feature.FavoriteDao

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}