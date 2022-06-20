package com.example.android.nextreminder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "similar_bookmark")
data class SimilarEntity(
    val name: String,
    val type: String,
    val description: String,
    val wikipediaUrl: String,
    val youtubeVideoUrl: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)