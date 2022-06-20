package com.example.android.nextreminder.data

enum class SimilarItemTypeEnum(val type: String) {

    MUSIC("music"),

    MOVIE("movie"),

    TVSHOW("show"),

    BOOK("book"),

    AUTHOR("author"),

    GAME("game"),

    PODCAST("podcast");

    companion object {
        fun getType(type: String): SimilarItemTypeEnum {
            return values().firstOrNull { it.type == type } ?: MUSIC
        }
    }
}