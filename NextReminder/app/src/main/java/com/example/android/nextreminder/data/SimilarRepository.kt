package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.network.SimilarService

class SimilarRepository(private val similarApi: SimilarService) {

    suspend fun getSimilarMedia(keyword: String): Pair<String, List<SimilarDTO>> {
        val queryList = mutableListOf<String>()
        val resultList = mutableListOf<SimilarDTO>()

        val response = similarApi.getSuggestions(query = keyword).result

        response.keywordList.forEach {
            queryList.add(it.name)
        }
        response.resultList?.forEach { item ->
            resultList.add(item.toDTO())
        }
        return Pair(queryList.joinToString(", "), resultList)
    }
}