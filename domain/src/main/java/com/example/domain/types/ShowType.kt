package com.example.domain.types

enum class ShowsType {
    Popular,
    Watched,
    Favorited,
    ComingSoon;

    // FIXME: spostare su presentatrion
//    val stringRes: Int
//        get() = when (this) {
//            Popular -> R.string.popular
//            Watched -> R.string.watched
//            Favorited -> R.string.favorited
//            ComingSoon -> R.string.anticipated
//        }

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            Watched -> "watched"
            Favorited -> "favorited"
            ComingSoon -> "anticipated"
        }

}