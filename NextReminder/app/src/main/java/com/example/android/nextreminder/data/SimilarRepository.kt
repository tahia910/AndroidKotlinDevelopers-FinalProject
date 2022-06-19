package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.network.Result
import com.example.android.nextreminder.data.network.SimilarService

class SimilarRepository(private val similarApi: SimilarService) {

    suspend fun getSimilarMedia(keyword: String): Result<Pair<String, List<SimilarDTO>>> {
        return try {
            val queryList = mutableListOf<String>()
            val resultList = mutableListOf<SimilarDTO>()

            val response = similarApi.getSuggestions(query = keyword).result

            response.keywordList.forEach {
                queryList.add(it.name)
            }
            response.resultList?.forEach { item ->
                resultList.add(item.toDTO())
            }
            Result.Success(Pair(queryList.joinToString(", "), resultList))
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
}