package com.example.android.nextreminder.data

class SimilarRepository (private val similarApi: SimilarService) {

    suspend fun getSimilarMedia(keyword: String): SimilarList {
        return similarApi.getSuggestions(query = keyword).result
    }
}