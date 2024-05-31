package com.example.muvitracker.inkotlin.domain.model


data class PrefsModel(
    val liked: Boolean,
    val watched: Boolean,
    val detail : DetailModel // o dto
    )