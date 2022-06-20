package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.local.SimilarEntity

data class SimilarDTO(
    val name: String,
    val type: SimilarItemTypeEnum,
    val description: String,
    val wikipediaUrl: String,
    val youtubeVideoUrl: String
) {
    fun toEntity(): SimilarEntity {
        return SimilarEntity(
            name = name,
            type = type.type,
            description = description,
            wikipediaUrl = wikipediaUrl,
            youtubeVideoUrl = youtubeVideoUrl
        )
    }
}