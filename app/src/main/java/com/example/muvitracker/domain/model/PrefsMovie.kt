package com.example.muvitracker.domain.model


data class PrefsMovie(
    val liked: Boolean,
    val watched: Boolean,
    val detail : DetailMovie // o dto
    )