package com.example.core

enum class ShowsType {
    Popular,
    Watched,
    Favorited,
    ComingSoon;

    val stringRes: Int
        get() = when (this) {
            Popular -> R.string.popular
            Watched -> R.string.watched
            Favorited -> R.string.favorited
            ComingSoon -> R.string.anticipated
        }

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            Watched -> "watched"
            Favorited -> "favorited"
            ComingSoon -> "anticipated"
        }

}