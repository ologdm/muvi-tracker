package com.example.muvitracker.data.prefs

import com.example.muvitracker.data.detail.DetailEntity
import com.example.muvitracker.domain.model.DetailMovie

data class PrefsEntity(
    val liked: Boolean,
    val watched: Boolean,
    val movieId: Int
)




