package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.domain.model.DetailMovie
import com.google.gson.annotations.SerializedName

// old
//@Entity(tableName = "detail_movie_entities")
//data class DetailMovieEntity(
//    @PrimaryKey val traktId: Int,
//    val title: String,
//    val year: Int,
//    @TypeConverters(ConvertersUtils::class) val ids: Ids,
//
//    val tagline: String, // new
//    val overview: String,
//    val released: String?,
//    val runtime: Int,
//    val country: String,
//    val trailer: String, // new
//    val homepage: String, // new
//    val status: String, // new
//    val rating: String,
//    val votes: Int, // new
//    val language: String, // new
//    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
//)


// TODO new, READY
@Entity(tableName = "detail_movie") // old detail_movie_entities
data class DetailMovieEntity(
    // trakt base
    @PrimaryKey val traktId: Int,
    val year: Int?, // 2016
    @TypeConverters(ConvertersUtils::class) val ids: Ids,
    // tmdb
    val title: String?,
    val tagline: String?,
    val overview: String?,
    val status: String?, // released
    val releaseDate: String?, // 2016-02-12
    val country: String?,
    val runtime: Int?,
    val originalLanguage: String?, // "en" // modif: language->originalLanguage
    val originalTitle: String?, // new
    @TypeConverters(ConvertersUtils::class) val genres: List<String>, // not null !!
    val youtubeTrailer: String?, // https://youtube.com/watch?v=9vN6DHB6bJc
    val homepage: String?, // http://www.20thcenturystudios.com/movies/deadpool
    // other
    val backdropPath: String?, // new
    val posterPath: String?, // new

    // rating
    val traktRating: String?, // modif: rating->traktRating
    val tmdbRating: String?, // new

    // da sistema
    val currentTranslation: String, // not null, new


    // TODO  other ratings - Imdb, Metacritic, Rotten Tomatoes
    )


// (PrefsEntity?) - can be null as logic
fun DetailMovieEntity.toDomain(prefsMovieEntity: PrefsMovieEntity?): DetailMovie {
    return DetailMovie(
        title = title ?: "",
        year = year ?: 0,
        ids = ids,
        tagline = tagline?: "",
        overview = overview?: "",
        released = releaseDate,
        runtime = runtime ?: 0,
        country = country?: "",
        trailer = youtubeTrailer?: "",
        homepage = homepage?: "" ,
        status = status ?: "",
        rating = traktRating.toString(),
        votes = 100, // provisorio
        language = originalLanguage ?: "",
        genres = genres,
        // prefs
        liked = prefsMovieEntity?.liked ?: false,
        watched = prefsMovieEntity?.watched ?: false,
        addedDateTime = prefsMovieEntity?.addedDateTime // Timestamp when added to db
    )
}