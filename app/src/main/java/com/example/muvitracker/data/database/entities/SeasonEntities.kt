package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.WatchedDataModel
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.domain.model.SeasonExtended

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
)


//  todo !!!
fun SeasonEntity.copyDtoData(seasonDto: SeasonExtenDto): SeasonEntity {
    return this.copy(
        seasonTraktId = seasonDto.ids.trakt,
        seasonNumber = seasonDto.number,
        ids = seasonDto.ids,
        rating = seasonDto.rating,
        episodeCount = seasonDto.episodeCount,
        airedEpisodes = seasonDto.airedEpisodes,
        title = seasonDto.title,
        overview = seasonDto.overview ?: "",
        releaseYear = seasonDto.getYear(),
        network = seasonDto.network
        // showId - already exist
    )
}


