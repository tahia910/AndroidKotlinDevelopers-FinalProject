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
            return when (type) {
                MOVIE.type -> MOVIE
                TVSHOW.type -> TVSHOW
                BOOK.type -> BOOK
                AUTHOR.type -> AUTHOR
                GAME.type -> GAME
                PODCAST.type -> PODCAST
                else -> MUSIC
            }
        }
    }
}