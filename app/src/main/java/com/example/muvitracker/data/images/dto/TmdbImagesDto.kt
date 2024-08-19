package com.example.muvitracker.data.images.dto

import com.google.gson.annotations.SerializedName

data class MediaItem(
    @SerializedName("aspect_ratio") val aspectRatio: Double = 0.0,
    val height: Int = 0,
    @SerializedName("iso_639_1") val iso6391: String? = null,
    @SerializedName("file_path") val filePath: String = "", // "https://image.tmdb.org/t/p/original + ${/en971MEXui9diirXlogOrPKmsEn.jpg}"
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    val width: Int = 0,
)


// movie, show
data class MovieShowImagesDto(
    val backdrops: List<MediaItem> = listOf(), // orrizontale 16:9 (come 1920x1080 pixel).
    val id: Int = 0, // tmdb movie id
//    val logos: List<MediaItem> = listOf(), // - non serve
    val posters: List<MediaItem> = listOf() // verticale,  2:3 (come 1000x1500 pixel) aspect_ratio": 0.667
)

// season
data class SeasonImageDto(
    val id: Int = 0,
    val posters: List<MediaItem> = listOf()
)

// episode
data class EpisodeImageDto(
    val id: Int = 0,
    val stills: List<MediaItem> = listOf()
)


// person
data class PersonImageDto(
    val id: Int = 0,
    val profiles: List<MediaItem> = listOf()
)

