package com.alperenturker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alperenturker.core.database.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY title ASC")
    fun observeAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT COUNT(1) FROM bookmarks WHERE imdbID = :id")
    suspend fun isBookmarked(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE imdbID = :id")
    suspend fun remove(id: String)
}