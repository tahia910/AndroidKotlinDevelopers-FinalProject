package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.network.Result
import com.example.android.nextreminder.data.network.image.ImageService

class ImageRepository(private val imageApi: ImageService) {

    suspend fun getRelatedImages(keywords: String): Result<List<ImageDTO>> {
        return try {
            val response = imageApi.getRelatedImages(keywords).results
            if (response.isEmpty()) return Result.Success(emptyList())

            val list = response.mapIndexed { index, url ->
                ImageDTO(url, index)
            }

            Result.Success(list)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
}