package com.example.domain

// NOTE: story persa, scorporato da viewmodel
enum class MovieType {
    Popular,
    BoxOffice,
    Watched,
    Favorited,
    ComingSoon;


    // FIXME: spostare su presentatrion
//    val stringRes: Int
//        get() = when (this) {
//            Popular -> R.string.popular
//            BoxOffice -> R.string.box_office
//            Watched -> R.string.watched
//            Favorited -> R.string.favorited
//            ComingSoon -> R.string.anticipated
//        }

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            BoxOffice -> "boxoffice"
            Watched -> "watched"
            Favorited -> "favorited"
            ComingSoon -> "anticipated"
        }

}