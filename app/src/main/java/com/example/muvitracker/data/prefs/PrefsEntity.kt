package com.example.muvitracker.data.prefs

// solo stati & id movie,
// utilizzerà DetailDomain per vedere oggetto
// TODO imdb per immagine


data class PrefsEntity(
    val liked: Boolean,
    val watched: Boolean,
    val movieId: Int

)



