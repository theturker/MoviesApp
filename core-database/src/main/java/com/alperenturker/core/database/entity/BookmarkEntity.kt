package com.alperenturker.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: String?,
    val poster: String?
)
