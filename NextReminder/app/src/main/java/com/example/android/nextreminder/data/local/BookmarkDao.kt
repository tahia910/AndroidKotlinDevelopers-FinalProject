package com.example.android.nextreminder.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM similar_bookmark ORDER BY name ASC")
    suspend fun getAllBookmarks(): List<SimilarEntity>

    @Query("SELECT EXISTS(SELECT * FROM similar_bookmark WHERE name = :name AND type = :type AND description = :description)")
    suspend fun isBookmarked(name: String, type: String, description: String): Boolean

    @Insert(onConflict = REPLACE)
    suspend fun insertBookmark(vararg similarEntity: SimilarEntity)

    @Delete
    suspend fun deleteBookmark(similarEntity: SimilarEntity)

    suspend fun getAlreadyBookmarkedFromList(list: List<SimilarEntity>): List<SimilarEntity>? {
        val result = mutableListOf<SimilarEntity>()

        list.forEach { item ->
            if (isBookmarked(item.name, item.type, item.description)) {
                result.add(item)
            }
        }
        return result
    }
}