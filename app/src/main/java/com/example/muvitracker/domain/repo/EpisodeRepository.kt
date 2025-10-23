package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    suspend fun fetchSeasonEpisodes(
        showIds: Ids,
        seasonNr: Int
    )

    fun getSeasonAllEpisodesFlow(
        showIds: Ids,
        seasonNr: Int
    ): Flow<IoResponse<List<EpisodeExtended>>>

    fun getSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ): Flow<EpisodeExtended?>

    suspend fun toggleSingleWatchedEpisode(
        showIds: Ids,
        seasonNr: Int,
        episodeNr: Int
    )

}