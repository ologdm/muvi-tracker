package com.example.domain.repo

import com.example.domain.model.Episode
import com.example.domain.model.Ids
import com.example.domain.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    suspend fun fetchSeasonEpisodes(
        showIds: Ids,
        seasonNr: Int
    )

    fun getSeasonAllEpisodesFlow(
        showIds: Ids,
        seasonNr: Int
    ): Flow<IoResponse<List<Episode>>>

    fun getSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ): Flow<Episode?>

    suspend fun toggleSingleWatchedEpisode(
        showIds: Ids,
        seasonNr: Int,
        episodeNr: Int
    )

}