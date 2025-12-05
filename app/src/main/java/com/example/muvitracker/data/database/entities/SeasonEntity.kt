package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.dto.season.SeasonTmdbDto
import com.example.muvitracker.data.dto.season.SeasonTraktDto
import com.example.muvitracker.data.dto.season.mergeSeasonsDtoToEntity
import com.example.muvitracker.data.dto._support.Ids


// 1.1.3 OK
@Entity(tableName = "season_table")
data class SeasonEntity(
    // base data - from trakt dto
    @PrimaryKey val seasonTraktId: Int,
    val seasonNumber: Int, // X
    val ids: Ids,
    val showId: Int, // X, passed through parameter .toEntity(showId)
    /** episodeCount, airedEpisodes -> default=0, serve per evitare crash nei calcolo  */
    val episodeCount: Int = 0, // totali stagione
    val airedEpisodes: Int = 0, // usciti stagione
    val network: String?, //  * su tmdb List<Network> non è disponibile nella chiamata

    // tmdb ---------------------
    val title: String?,
    val overview: String?,
    val airDate: String?,
    val posterPath: String?,

    val traktRating: String?,
    val tmdbRating: String?, // da double

    // da sistema
    val currentTranslation: String, // not null
)


// not used
fun SeasonEntity.copyDtosData(
    trakt: SeasonTraktDto,
    tmdb: SeasonTmdbDto?
): SeasonEntity {
    val showId = this.showId
    return mergeSeasonsDtoToEntity(showId,trakt,tmdb)
}




