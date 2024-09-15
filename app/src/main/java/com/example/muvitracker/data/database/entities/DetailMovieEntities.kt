package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.domain.model.DetailMovie


@Entity(tableName = "detail_movie_entities")
data class DetailMovieEntity(
    @PrimaryKey val traktId: Int,
    val title: String,
    val year: Int,
    @TypeConverters(ConvertersUtils::class) val ids: Ids,

//    val tagline: String, // TODO
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
//    val trailer: String, TODO
//    val homepage: String, TODO
//    val status: String, TODO
    val rating: Float,
//    val votes: Int, // TODO
//    val language: String, // TODO
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
)


// (PrefsEntity?) - can be null as logic
fun DetailMovieEntity.toDomain(prefsMovieEntity: PrefsMovieEntity?): DetailMovie {
    return DetailMovie(
        title = title,
        year = year,
        ids = ids,
        overview = overview,
        released = released,
        runtime = runtime,
        country = country,
        rating = rating,
        genres = genres,

        liked = prefsMovieEntity?.liked ?: false,
        watched = prefsMovieEntity?.watched ?: false,
        addedDateTime = prefsMovieEntity?.addedDateTime
    )
}