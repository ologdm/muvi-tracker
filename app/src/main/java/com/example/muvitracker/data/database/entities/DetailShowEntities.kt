package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.DetailShow
import com.google.gson.annotations.SerializedName

// 00
@Entity(tableName = "DetailShowEntities")
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
    //
    val trailer: String,
    val homepage: String,
    val status: String,
    val rating: Float,
    val votes: Int,
    val language: String,
    val languages: List<String>,
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
    val airedEpisodes: Int
)


// 00
// (PrefsEntity?) - can be null as logic
fun DetailShowEntity.toDomain(prefsShowEntity: PrefsShowEntity?): DetailShow {
    return DetailShow(
        title = title,
        year = year,
        ids = ids,
        //
        tagline = tagline,
        overview = overview,
        firstAired = firstAired,
        runtime = runtime,
        network = network,
        country = country,
        //
        trailer = trailer,
        homepage = homepage,
        status = status,
        rating = rating,
        votes = votes,
        //
        language = language,
        languages = languages,
        genres = genres,
        airedEpisodes = airedEpisodes,
        ////// prefs
        liked = prefsShowEntity?.liked ?: false,
        watchedAll = prefsShowEntity?.watchedAll ?: false,
        watchedCount = prefsShowEntity?.watchedCount ?: 0,
        addedDateTime = prefsShowEntity?.addedDateTime
    )
}


// if(tutteLeStagioniWatchedAll==true){
        //watchedAll==true
// }