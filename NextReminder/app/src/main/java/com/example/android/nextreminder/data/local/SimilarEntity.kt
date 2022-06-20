package com.example.android.nextreminder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarItemTypeEnum

@Entity(tableName = "similar_bookmark")
data class SimilarEntity(
    val name: String,
    val type: String,
    val description: String,
    val wikipediaUrl: String,
    val youtubeVideoUrl: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    fun toDto(): SimilarDTO {
        return SimilarDTO(
            name = name,
            type = SimilarItemTypeEnum.getType(type),
            description = description,
            wikipediaUrl = wikipediaUrl,
            youtubeVideoUrl = youtubeVideoUrl
        )
    }
}

fun List<SimilarEntity>.entityToDtoList(): List<SimilarDTO> {
    return map { it.toDto() }
}