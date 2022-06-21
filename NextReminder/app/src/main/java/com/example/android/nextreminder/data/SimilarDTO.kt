package com.example.android.nextreminder.data

import com.example.android.nextreminder.data.local.SimilarEntity

data class SimilarDTO(
    val name: String,
    val type: SimilarItemTypeEnum,
    val description: String,
    val wikipediaUrl: String,
    val youtubeVideoUrl: String,
    var isBookmarked: Boolean = false
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

fun SimilarDTO.isSameAs(item: SimilarDTO): Boolean {
    return this.name == item.name && this.type == item.type && this.description == item.description
}