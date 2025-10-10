package com.example.muvitracker.data.dto.tmdb.old

import com.example.muvitracker.data.database.all_images_tmdb.database.entities.EpisodeImageEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.MovieShowImageEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.PersonImageEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.SeasonImageEntity
import com.google.gson.annotations.SerializedName

// tmdb - id univoco tra movie,show, season, episode


// IMAGE DTO
data class MediaItem(
    @SerializedName("aspect_ratio") val aspectRatio: Double = 0.0,
    val height: Int = 0,
    @SerializedName("iso_639_1") val iso6391: String? = null,
    @SerializedName("file_path") val filePath: String = "", // "https://image.tmdb.org/t/p/original + ${/en971MEXui9diirXlogOrPKmsEn.jpg}"
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    val width: Int = 0,
)


// 1. MOVIE, SHOW  ################################################################################
data class MovieShowImagesDto(
    val id: Int = 0, // tmdb movie id
    val backdrops: List<MediaItem> = listOf(), // orrizontale 16:9 (come 1920x1080 pixel).
    val posters: List<MediaItem> = listOf() // verticale,  2:3 (come 1000x1500 pixel) aspect_ratio": 0.667
    //    val logos: List<MediaItem> = listOf(), // - non serve
)

fun MovieShowImagesDto.toEntity(): MovieShowImageEntity {
    return MovieShowImageEntity(
        id = id,
        backdrops = backdrops,
        posters = posters
    )
}


// 2. SEASON  ################################################################################
data class SeasonImageDto(
    val id: Int = 0,
    val posters: List<MediaItem> = listOf()
)


fun SeasonImageDto.toEntity(tmdbShowId: Int, seasonNr: Int): SeasonImageEntity {
    return SeasonImageEntity(
        id = id, // todo fare - seasonTmdbId??
        posters = posters,
        tmdbShowId = tmdbShowId,
        seasonNumber = seasonNr
    )
}


// 3. EPISODE  ################################################################################
data class EpisodeImageDto(
    val id: Int = 0,
    val stills: List<MediaItem> = listOf()
)

fun EpisodeImageDto.toEntity(
    tmdbShowId: Int,
    seasonNr: Int,
    episodeNr: Int
): EpisodeImageEntity {
    return EpisodeImageEntity(
        id = id,
        stills = stills,
        tmdbShowId = tmdbShowId,
        seasonNumber = seasonNr,
        episodeNumber = episodeNr
    )
}


// 4. PERSON  ################################################################################ todo
data class PersonImageDto(
    val id: Int = 0,
    val profiles: List<MediaItem> = listOf()
)

fun PersonImageDto.toEntity(): PersonImageEntity {
    return PersonImageEntity(
        id = id,
        profiles = profiles
    )
}


// UTILS ###########################################################
fun List<MediaItem>.filterNotUhdBackdrops(): List<MediaItem> {
    return this.filter {
        it.width <= 3500
    }
}

fun List<MediaItem>.filterNotUhdPosters(): List<MediaItem> {
    return this.filter {
        it.width <= 3500
    }
}

