package com.example.android.nextreminder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// TODO: delete "exportSchema = false" when stable
@Database(entities = [SimilarEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}

object LocalDatabase {
    fun createBookmarkDao(context: Context): BookmarkDao {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "nextReminder.db"
        ).build().bookmarkDao()
    }
}