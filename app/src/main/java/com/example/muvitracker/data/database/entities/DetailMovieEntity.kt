package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.DetailMovie
import kotlin.collections.joinToString

// all elements - null
// lists notnull
// currentTranslation - notnull

@Entity(tableName = "detail_movie")
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
    @TypeConverters(ConvertersUtils::class) val country: List<String>, // not null
    val runtime: Int?, // 106
    val originalLanguage: String?, // en
    val originalTitle: String?, // Deadpool
    @TypeConverters(ConvertersUtils::class) val genres: List<String>, // azione, storico - not null
    val youtubeTrailer: String?, // https://youtube.com/watch?v=9vN6DHB6bJc
    val homepage: String?, // http://www.20thcenturystudios.com/movies/deadpool
    // path link immagini
    val backdropPath: String?,  // /en971MEXui9diirXlogOrPKmsEn.jpg
    val posterPath: String?,    // /zoSiiUUzg2ny6uzuil7PbP13z53.jpg

    // ratings
    val traktRating: String?, // 8.3
    val tmdbRating: String?, // 7.9
    // TODO  other ratings - Imdb, Metacritic, Rotten Tomatoes

    // da sistema
    val currentTranslation: String, // not null, new

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
        country = country.joinToString(", "),
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