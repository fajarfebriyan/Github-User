package com.astro.test.fajarfebriyan.data.database

import com.astro.test.fajarfebriyan.core.db.AppDatabase
import com.astro.test.fajarfebriyan.data.model.Favorite

interface DatabaseHelper {

    suspend fun getFavList(): List<Favorite>
    suspend fun insertFav(fav: Favorite)
    suspend fun deleteFav(fav: Favorite)

    class DatabaseHelperImpl(private val appDatabase: AppDatabase): DatabaseHelper {
        override suspend fun getFavList() = appDatabase.favoriteDao().getFavList()

        override suspend fun insertFav(fav: Favorite) = appDatabase.favoriteDao().insertFav(fav)

        override suspend fun deleteFav(fav: Favorite) = appDatabase.favoriteDao().deleteFav(fav)
    }
}