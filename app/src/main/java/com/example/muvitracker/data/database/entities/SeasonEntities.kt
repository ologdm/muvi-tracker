package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.utils.firstDecimalApproxToString

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
    val rating: String? = null,
    val episodeCount: Int? = null,
    val airedEpisodes: Int? = null,
    val title: String? = null,
    val overview: String? = null,
    val releaseYear: String? = null,
    val network: String? = null,
)


//  update entity - only dto part
fun SeasonEntity.copyDtoData(seasonDto: SeasonExtenDto): SeasonEntity {
    return this.copy(
        seasonTraktId = seasonDto.ids.trakt,
        seasonNumber = seasonDto.number ?: 0,
        ids = seasonDto.ids,
        rating = seasonDto.rating?.firstDecimalApproxToString() ?: "0.0",
        episodeCount = seasonDto.episodeCount ?: 0,
        airedEpisodes = seasonDto.airedEpisodes ?: 0 ,
        title = seasonDto.title ?: "N/A",
        overview = seasonDto.overview ?: "N/A",
        releaseYear = seasonDto.getYear() ?: "N/A",
        network = seasonDto.network ?: "N/A"
        // showId - already exist
    )
}


