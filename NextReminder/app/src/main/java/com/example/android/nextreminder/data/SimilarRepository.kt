package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.local.BookmarkDao
import com.example.android.nextreminder.data.local.entityToDtoList
import com.example.android.nextreminder.data.network.Result
import com.example.android.nextreminder.data.network.SimilarService
import com.example.android.nextreminder.data.network.toDtoList

class SimilarRepository(
    private val similarApi: SimilarService,
    private val bookmarkDao: BookmarkDao
) {

    suspend fun getSimilarMedia(keyword: String): Result<Pair<String, List<SimilarDTO>>> {
        return try {
            val response = similarApi.getSuggestions(query = keyword).result

            val queryList = response.keywordList.map { it.name }
            val resultList = response.resultList?.toDtoList() ?: emptyList()

            Result.Success(Pair(queryList.joinToString(", "), resultList))
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

//    suspend fun getAllBookmarks(): Result<List<SimilarDTO>> {
//        return try {
//            val list = bookmarkDao.getAllBookmarks()
//            Result.Success(list.entityToDtoList())
//        } catch (ex: Exception) {
//            Result.Error(ex.localizedMessage)
//        }
//    }

    fun getAllBookmarks() = bookmarkDao.getAllBookmarks()

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
            bookmarkDao.findAndDeleteBookmark(item.toEntity())
            Result.Success(item)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
}