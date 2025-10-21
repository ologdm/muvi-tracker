package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.dto.season.SeasonTmdbDto
import com.example.muvitracker.data.dto.season.SeasonTraktDto
import com.example.muvitracker.data.dto.season.mergeSeasonsDtoToEntity
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.utils.firstDecimalApproxToString

// completo - apertura detail
// parziale - da mylist, watchedAll

// old
//@Entity(tableName = "season_entities")
//data class SeasonEntity(
//    // base data - from base dto
//    @PrimaryKey val seasonTraktId: Int,
//    val seasonNumber: Int, // X
//    val ids: Ids,
//    val showId: Int, // X, passed through parameter .toEntity(showId)
//
//    // todo - test salvataggio parziale
//    val rating: String? = null,
//    val episodeCount: Int? = null,
//    val airedEpisodes: Int? = null,
//    val title: String? = null,
//    val overview: String? = null,
//    val releaseYear: String? = null,
//    val network: String? = null,
//)


// new
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




