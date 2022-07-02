package com.example.android.nextreminder.data.network.similar

import com.example.android.nextreminder.data.network.SimilarResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SimilarService {

    @GET("api/similar")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("type") type: String,
        // Get detailed response (not just name/type)
        @Query("info") verbose: Int = 1
    ): SimilarResponse
}