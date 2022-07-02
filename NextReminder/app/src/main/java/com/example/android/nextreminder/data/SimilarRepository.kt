package com.example.android.nextreminder.data

import androidx.core.text.htmlEncode
import com.example.android.nextreminder.data.local.BookmarkDao
import com.example.android.nextreminder.data.network.Result
import com.example.android.nextreminder.data.network.similar.SimilarService
import com.example.android.nextreminder.data.network.toDtoList

class SimilarRepository(
    private val similarApi: SimilarService,
    private val bookmarkDao: BookmarkDao
) {

    /**
     ** The keyword list is searched in TasteDive's database, and if one or several medias are
     * found, they return similar medias with the same type.
     * https://tastedive.com/read/api?ref=publicapis.dev
     */
    suspend fun getSimilarMediaList(keywords: String, filter: SimilarItemTypeEnum):
            Result<List<SimilarDTO>> {
        return try {
            val query = buildQueryString(keywords, filter)

            val response = similarApi.getSuggestions(query = query, filter.query).result

            val resultList = response.resultList?.toDtoList() ?: return Result.Success(emptyList())

            // Update the bookmarks before returning the list to the ViewModel.
            val bookmarkList = bookmarkDao.getAlreadyBookmarkedItemsFromList(resultList)
            if (!bookmarkList.isNullOrEmpty()) {
                resultList.map { item ->
                    if (bookmarkList.contains(item)) item.isBookmarked = true
                }
            }

            Result.Success(resultList)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    /**
     * Define the query and result type.
     * The doc seems to imply that query and result types could be different,
     * but after many experimentation, it doesn't seem to be true.
     */
    private fun buildQueryString(keywords: String, filter: SimilarItemTypeEnum): String {
        var query = ""
        val keywordList = keywords.split(",")
        keywordList.forEach { keyword ->
            if (keyword.trim(' ').isNotBlank()) {
                // Format example: "movie:harry potter, movie:trainspotting"
                query += "${filter.type}:${keyword.trim(' ')}, "
            }
        }
        return query.trim(' ', ',').htmlEncode()
    }

    fun getAllBookmarksLiveData() = bookmarkDao.getAllBookmarksLiveData()

    suspend fun addBookmark(item: SimilarDTO): Result<SimilarDTO> {
        return try {
            bookmarkDao.insertBookmark(item.toEntity())
            Result.Success(item)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    suspend fun removeBookmark(item: SimilarDTO): Result<SimilarDTO> {
        return try {
            bookmarkDao.findAndDeleteBookmark(item)
            Result.Success(item)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
}