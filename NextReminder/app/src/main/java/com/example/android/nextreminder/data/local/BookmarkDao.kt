package com.example.android.nextreminder.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM similar_bookmark ORDER BY name ASC")
    fun getAllBookmarks(): LiveData<List<SimilarEntity>>

    @Query("SELECT EXISTS(SELECT * FROM similar_bookmark WHERE name = :name AND type = :type AND description = :description)")
    suspend fun isBookmarked(name: String, type: String, description: String): Boolean

    @Insert(onConflict = REPLACE)
    suspend fun insertBookmark(vararg similarEntity: SimilarEntity)

    @Query("SELECT * FROM similar_bookmark WHERE name = :name AND type = :type AND description = :description")
    suspend fun getBookmark(name: String, type: String, description: String): SimilarEntity

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

    suspend fun findAndDeleteBookmark(item: SimilarEntity): SimilarEntity {
        val itemInDb = getBookmark(item.name, item.type, item.description)
        deleteBookmark(itemInDb)
        return itemInDb
    }
}