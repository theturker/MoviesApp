package com.alperenturker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alperenturker.core.database.dao.BookmarkDao
import com.alperenturker.core.database.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
