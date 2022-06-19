package com.example.android.nextreminder.data.network

import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.squareup.moshi.Json

data class SimilarResponse(
    @Json(name = "Similar")
    val result: SimilarList
)

data class SimilarList(
    @Json(name = "Info")
    val keywordList: List<SimilarItem>, // The original request
    @Json(name = "Results")
    val resultList: List<SimilarItem>?
)

data class SimilarItem(
    @Json(name = "Name")
    val name: String,
    @Json(name = "Type")
    val type: String,
    @Json(name = "mTeaser")
    val description: String? = null,
    @Json(name = "wUrl")
    val wikipediaUrl: String? = null,
    @Json(name = "yUrl")
    val youtubeVideoUrl: String? = null,
    @Json(name = "iID")
    val youtubeVideoId: String? = null
) {
    fun toDTO(): SimilarDTO {
        return SimilarDTO(
            name = name,
            type = SimilarItemTypeEnum.getType(type),
            description = description ?: "",
            wikipediaUrl = wikipediaUrl ?: "",
            youtubeVideoUrl = youtubeVideoUrl ?: ""
        )
    }
}