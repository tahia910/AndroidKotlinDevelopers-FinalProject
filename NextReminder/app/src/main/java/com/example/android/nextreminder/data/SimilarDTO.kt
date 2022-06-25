package com.example.android.nextreminder.data

import android.os.Parcelable
import com.example.android.nextreminder.data.local.SimilarEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SimilarDTO(
    val name: String,
    val type: SimilarItemTypeEnum,
    val description: String,
    val wikipediaUrl: String,
    val youtubeVideoUrl: String,
    var isBookmarked: Boolean = false
): Parcelable {
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

/**
 * Due to the bookmark statuses, we can't compare 2 items directly.
 * We want to make sure the item is roughly the same (in case a movie and a song have the same name),
 * so we also use the type and description in the comparison.
 */
fun SimilarDTO.isSameAs(item: SimilarDTO): Boolean {
    return this.name == item.name && this.type == item.type && this.description == item.description
}