package com.example.android.nextreminder.data.network.image

import com.squareup.moshi.Json

data class ImageResponse(
    @Json(name = "image_name")
    val imageName: String,
    val results: List<String>
)