package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ComicEntity::class], version = 1, exportSchema = false)
@TypeConverters(ComicConverters::class)
abstract class ComicDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao

    companion object {
        @Volatile
        private var INSTANCE: ComicDatabase? = null

        fun getInstance(context: Context): ComicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ComicDatabase::class.java,
                    "comiccraft.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
