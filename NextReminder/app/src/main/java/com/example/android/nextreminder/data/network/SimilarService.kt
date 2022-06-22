package com.example.android.nextreminder.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface SimilarService {

    @GET("similar")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("type") type: String,
        // Get detailed response (not just name/type)
        @Query("info") verbose: Int = 1
    ): SimilarResponse
}