package com.astro.test.fajarfebriyan.core.db

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {

    @Volatile private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null){
            synchronized(AppDatabase::class){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database.db").build()
            }
        }
        return INSTANCE!!
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}