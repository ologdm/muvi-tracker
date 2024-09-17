package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.base.Ids

// completo - apertura detail
// parziale - da mylist, watchedAll

@Entity(tableName = "season_entities")
data class SeasonEntity(
    // base data - from base dto
    @PrimaryKey val seasonTraktId: Int,
    val seasonNumber: Int, // X
    val ids: Ids,
    val showId: Int, // X, passed through parameter .toEntity(showId)

    // todo - test salvataggio parziale
    val rating: Double? = null,
    val episodeCount: Int? = null,
    val airedEpisodes: Int? = null,
    val title: String? = null,
    val overview: String? = null,
    val releaseYear: String? = null,
    val network: String? = null,

    // save
    var watchedAll: Boolean = false,
    var watchedCount: Int = 0
)


// TODO fare ck se corretto !!
fun SeasonEntity.copyDtoData(dto: SeasonExtenDto): SeasonEntity {
    return this.copy(
        seasonTraktId = dto.ids.trakt,
        seasonNumber = dto.number,
        ids = dto.ids,
        rating = dto.rating,
        episodeCount = dto.episodeCount,
        airedEpisodes = dto.airedEpisodes,
        title = dto.title,
        overview = dto.overview ?: "",
        releaseYear = dto.getYear(),
        network = dto.network,
        showId = showId
    )
}


// from extended dto old
//    val rating: Double, // converted
//    val episodeCount: Int, // !! usare su detailShow-seasonlist-conteggio top
//    val airedEpisodes: Int, // non usare
//    val title: String,
//    val overview: String,
//    val releaseYear: String, // converted
//    val network: String,

// if (watchedCount==episodeCount){
//    watchedAll==true
// }

