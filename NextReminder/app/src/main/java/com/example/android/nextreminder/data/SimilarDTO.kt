package com.example.android.nextreminder.data

data class SimilarDTO(
    val name: String,
    val type: SimilarItemTypeEnum,
    val description: String,
    val wikipediaUrl: String,
    val youtubeVideoUrl: String
)