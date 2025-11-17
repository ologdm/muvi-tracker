package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.Movie

// all elements - null
// lists notnull
// currentTranslation - notnull

@Entity(tableName = "movie_table")
data class MovieEntity(
    // trakt base
    @PrimaryKey val traktId: Int,
    val year: Int?, // 2016
    @TypeConverters(ConvertersUtils::class) val ids: Ids,

    // tmdb
    val title: String?,
    val tagline: String?,
    val overview: String?,
    val status: String?, // released
    val releaseDate: String?, // output - 2016-02-12
    @TypeConverters(ConvertersUtils::class) val countries: List<String>, // not null
    val runtime: Int?, // 106
    val originalLanguage: String?, // en
    val originalTitle: String?, // Deadpool
    val englishTitle: String?, // Deadpool, english, from trakt
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
fun MovieEntity.toDomain(prefsMovieEntity: PrefsMovieEntity?): Movie {
    return Movie(
        year = year,
        ids = ids,

        title = title,
        tagline = tagline,
        overview = overview,
        status = status,
        releaseDate = releaseDate,
        runtime = runtime,
        originalLanguage = originalLanguage,
        englishTraktTitle = englishTitle,
        countries = countries,
        youtubeTrailer = youtubeTrailer,
        homepage = homepage,
        traktRating = traktRating,
        tmdbRating = tmdbRating,
        genres = genres,
        // servono su Domain?
        backdropPath = backdropPath,
        posterPath = posterPath,

        // prefs
        liked = prefsMovieEntity?.liked ?: false, // in caso manca prefs
        watched = prefsMovieEntity?.watched ?: false, // in caso manca prefs
        addedDateTime = prefsMovieEntity?.addedDateTime, // Timestamp when added to db
        notes = prefsMovieEntity?.notes ?: ""
    )
}