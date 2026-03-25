package com.example.domain.types

enum class ShowsType {
    Popular,
    Watched,
    Favorited,
    ComingSoon;

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            Watched -> "watched"
            Favorited -> "favorited"
            ComingSoon -> "anticipated"
        }

}