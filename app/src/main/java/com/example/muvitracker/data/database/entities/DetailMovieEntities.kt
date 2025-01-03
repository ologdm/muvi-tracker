package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.domain.model.DetailMovie


@Entity(tableName = "detail_movie_entities")
data class DetailMovieEntity(
    @PrimaryKey val traktId: Int,
    val title: String,
    val year: Int,
    @TypeConverters(ConvertersUtils::class) val ids: Ids,
    //
    val tagline: String, // new
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
    val trailer: String, // new
    val homepage: String, // new
    val status: String, // new
    val rating: String,
    val votes: Int, // new
    val language: String, // new
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
)


// (PrefsEntity?) - can be null as logic
fun DetailMovieEntity.toDomain(prefsMovieEntity: PrefsMovieEntity?): DetailMovie {
    return DetailMovie(
        title = title,
        year = year,
        ids = ids,
        tagline = tagline,
        overview = overview,
        released = released,
        runtime = runtime,
        country = country,
        trailer = trailer,
        homepage = homepage,
        status = status,
        rating = rating,
        votes = votes,
        language = language,
        genres = genres,
        // prefs
        liked = prefsMovieEntity?.liked ?: false,
        watched = prefsMovieEntity?.watched ?: false,
        addedDateTime = prefsMovieEntity?.addedDateTime
    )
}