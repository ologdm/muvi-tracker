package com.example.muvitracker.data.glide

import com.example.muvitracker.data.LanguageManager


/**
 * RELEASE 1.1.3 - Update images when the system language changes.
 * Note: The key ID and language are used only for movies, shows, and seasons.
 */

sealed interface ImageTmdbRequest {
    data class MovieVertical(
        val movieId: Int,
        val language: String = LanguageManager.getAppLocaleLanguageTag()
    ) : ImageTmdbRequest


    data class MovieHorizontal(
        val movieId: Int,
        val language: String = LanguageManager.getAppLocaleLanguageTag()
    ) : ImageTmdbRequest


    data class ShowVertical(
        val showId: Int,
        val language: String = LanguageManager.getAppLocaleLanguageTag()
    ) : ImageTmdbRequest


    data class ShowHorizontal(
        val showId: Int,
        val language: String = LanguageManager.getAppLocaleLanguageTag()
    ) : ImageTmdbRequest


    data class Season(
        val showId: Int,
        val seasonNr: Int,
        val language: String = LanguageManager.getAppLocaleLanguageTag()
    ) : ImageTmdbRequest


    data class Episode(
        val showId: Int,
        val seasonNr: Int,
        val episodeNr: Int
    ) : ImageTmdbRequest


    data class Person(
        val personId: Int,
    ) : ImageTmdbRequest
}