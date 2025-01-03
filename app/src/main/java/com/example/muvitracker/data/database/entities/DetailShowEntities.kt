package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.domain.model.DetailShow

@Entity(tableName = "detail_show_entities")
data class DetailShowEntity(
    @PrimaryKey val traktId: Int,
    val title: String,
    val year: Int,
    @TypeConverters(ConvertersUtils::class) val ids: Ids,
    //
    val tagline: String,
    val overview: String,
    val firstAired: String,
    val runtime: Int,
    val network: String,
    val country: String,
    val trailer: String,
    val homepage: String,
    val status: String,
    val rating: String,
    val votes: Int,
    val language: String,
    val languages: List<String>,
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
    val airedEpisodes: Int
)


// (PrefsEntity?) - can be null as logic
fun DetailShowEntity.toDomain(prefsShowEntity: PrefsShowEntity?): DetailShow {
    return DetailShow(
        title = title,
        year = year,
        ids = ids,
        //
        tagline = tagline,
        overview = overview,
        runtime = runtime,
        network = network,
        country = country,
        trailer = trailer,
        homepage = homepage,
        status = status,
        rating = rating,
        votes = votes,
        language = language,
        languages = languages,
        genres = genres,
        airedEpisodes = airedEpisodes,
        // prefs
        liked = prefsShowEntity?.liked ?: false,
        addedDateTime = prefsShowEntity?.addedDateTime
    )
}
