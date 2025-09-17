package com.alperenturker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alperenturker.core.database.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY rowid DESC")
    fun observeAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT COUNT(*) FROM bookmarks WHERE imdbId = :id")
    suspend fun isBookmarked(id: String): Int

    // Detay sayfasında ikonun canlı değişmesi için çok faydalı:
    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE imdbId = :id)")
    fun isBookmarkedFlow(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE imdbId = :id")
    suspend fun remove(id: String)
}
