package com.example.presentation.utils

import com.example.domain.types.MovieType
import com.example.domain.types.ShowsType
import com.example.presentation.R


// NOTE: prima era "stringRes"

fun MovieType.toStringRes(): Int = when (this) {
    MovieType.Popular -> R.string.popular
    MovieType.BoxOffice -> R.string.box_office
    MovieType.Watched -> R.string.watched
    MovieType.Favorited -> R.string.favorited
    MovieType.ComingSoon -> R.string.anticipated
}


fun ShowsType.toStringRes(): Int = when (this) {
    ShowsType.Popular -> R.string.popular
    ShowsType.Watched -> R.string.watched
    ShowsType.Favorited -> R.string.favorited
    ShowsType.ComingSoon -> R.string.anticipated
}