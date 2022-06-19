package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.network.SimilarService

class SimilarRepository (private val similarApi: SimilarService) {

    suspend fun getSimilarMedia(keyword: String): List<SimilarDTO> {
        val dtoList = mutableListOf<SimilarDTO>()
        val response = similarApi.getSuggestions(query = keyword).result
        response.resultList?.forEach { item ->
            dtoList.add(item.toDTO())
        }
        return dtoList
    }
}