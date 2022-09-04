package com.astro.test.fajarfebriyan.feature

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.astro.test.fajarfebriyan.data.model.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getFavList(): List<Favorite>

    @Insert(onConflict = REPLACE)
    fun insertFav(fav: Favorite)

    @Delete
    fun deleteFav(fav: Favorite)
}