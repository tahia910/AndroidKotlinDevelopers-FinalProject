package com.example.android.nextreminder.data

/**
 * type = the value used during search to specify the type of the inputted media to search, and in
 * the response list
 * label = the filter name displayed to the user on the search screen
 * query = the value used during search to request only a certain type of media in the response
 */
enum class SimilarItemTypeEnum(val type: String, val label: String, val query: String) {

    MUSIC("music", "Music", "music"),

    MOVIE("movie", "Movies", "movies"),

    TVSHOW("show", "TV Shows", "shows"),

    BOOK("book", "Books", "books"),

    AUTHOR("author", "Authors", "authors"),

    GAME("game", "Games", "games"),

    PODCAST("podcast", "Podcasts", "podcasts");

    companion object {
        fun getType(type: String): SimilarItemTypeEnum {
            return values().firstOrNull { it.type == type } ?: MUSIC
        }

        fun getEnumFromLabel(label: String): SimilarItemTypeEnum {
            return values().firstOrNull { it.label == label } ?: MUSIC
        }
    }
}