package com.example.android.nextreminder.data.network.image

import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

    @GET("api/1")
    suspend fun getRelatedImages(
        @Query("q") query: String
    ): ImageResponse
}