package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.DetailShow

/**
 *  tutte i conteggi aired/all sonio fatti su dati di trakt
 */


@Entity(tableName = "detail_show_table")
data class DetailShowEntity(
    // trakt
    @PrimaryKey val traktId: Int,
    val year: Int?,
    @TypeConverters(ConvertersUtils::class) val ids: Ids,
    val airedEpisodes: Int, // not null -> default 0 -> serve per calcolo

    // tmdb
    val title: String?,
    val tagline: String?,
    val overview: String?,
    val status: String?,
    val firstAirDate: String?, // TODO da trakt
    val lastAirDate: String?,
    val runtime: Int?,
    @TypeConverters(ConvertersUtils::class)
    val countries: List<String>, // not null
    val originalLanguage: String?, // en
    @TypeConverters(ConvertersUtils::class)
    val languages: List<String>,  // not null
    val originalTitle: String?, // Deadpool
    @TypeConverters(ConvertersUtils::class)
    val networks: List<String>, // not null
    @TypeConverters(ConvertersUtils::class)
    val genres: List<String>, // not null
    val youtubeTrailer: String?,
    val homepage: String?,
    // path link immagini
    val backdropPath: String?,  // /en971MEXui9diirXlogOrPKmsEn.jpg
    val posterPath: String?,    // /zoSiiUUzg2ny6uzuil7PbP13z53.jpg

    // ratings
    val traktRating: String?, // 8.3
    val tmdbRating: String?, // 7.9
    // TODO  other ratings - Imdb, Metacritic, Rotten Tomatoes

    // da sistema
    val currentTranslation: String, // not null
)



// !! usato solo in prefs repo, su detail repo estraggo direttamwente il domain da dao !!

// (PrefsEntity?) - can be null as logic
fun DetailShowEntity.toDomain(prefsShowEntity: PrefsShowEntity?): DetailShow {
    return DetailShow(
        // trakt
        year = year,
        ids = ids,
        airedEpisodes,
        // tmdb
        title = title ?: "",
        tagline = tagline ?: "",
        overview = overview ?: "",
        status = status ?: "",
        //
        firstAirDate = firstAirDate ?: "",
        lastAirDate = lastAirDate,
        runtime = runtime,
        countries = countries, // entity not null
        originalLanguage = originalLanguage ?: "",
        networks = networks, // entity not null
        genres = genres, // entity not null
        youtubeTrailer = youtubeTrailer ?: "",
        homepage = homepage ?: "",
        //
        traktRating = traktRating ?: "",
        tmdbRating = tmdbRating ?: "",
        //
        backdropPath = backdropPath ?: "",
        posterPath = posterPath ?: "",
        //
        currentTranslation = currentTranslation,

        // prefs
        liked = prefsShowEntity?.liked ?: false,
        addedDateTime = prefsShowEntity?.addedDateTime // Timestamp when added to db
    )
}
