package com.example.muvitracker.data

data class WatchedDataModel(
    val watchedAll: Boolean,
    val watchedCount: Int,

    val showId: Int = -1, // default
    val seasonNumber: Int = -1 // default
)
