package com.example.core

enum class MovieType {
    Popular,
    BoxOffice,
    Watched,
    Favorited,
    ComingSoon;

    val stringRes: Int
        get() = when (this) {
            Popular -> R.string.popular
            BoxOffice -> R.string.box_office
            Watched -> R.string.watched
            Favorited -> R.string.favorited
            ComingSoon -> R.string.anticipated
        }

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            BoxOffice -> "boxoffice"
            Watched -> "watched"
            Favorited -> "favorited"
            ComingSoon -> "anticipated"
        }

}