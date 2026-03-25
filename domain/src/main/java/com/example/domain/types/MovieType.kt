package com.example.domain.types


enum class MovieType {
    Popular,
    BoxOffice,
    Watched,
    Favorited,
    ComingSoon;

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            BoxOffice -> "boxoffice"
            Watched -> "watched"
            Favorited -> "favorited"
            ComingSoon -> "anticipated"
        }

}