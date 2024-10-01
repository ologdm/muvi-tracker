package com.example.muvitracker.data.imagetmdb.glide


sealed interface ImageTmdbRequest {

    data class MovieVertical(
        val movieId: Int,
    ) : ImageTmdbRequest


    data class MovieHorizontal(
        val movieId: Int,
    ) : ImageTmdbRequest


    data class ShowVertical(
        val showId: Int,
    ) : ImageTmdbRequest


    data class ShowHorizontal(
        val showId: Int,
    ) : ImageTmdbRequest


    data class Season(
        val showId: Int,
        val seasonNr: Int
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