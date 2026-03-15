package com.example.domain.repo

import com.example.domain.model.Episode
import com.example.domain.model.IdsDomain
import com.example.domain.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    suspend fun fetchSeasonEpisodes(
        showIds: IdsDomain,
        seasonNr: Int
    )

    fun getSeasonAllEpisodesFlow(
        showIds: IdsDomain,
        seasonNr: Int
    ): Flow<IoResponse<List<Episode>>>

    fun getSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ): Flow<Episode?>

    suspend fun toggleSingleWatchedEpisode(
        showIds: IdsDomain,
        seasonNr: Int,
        episodeNr: Int
    )

}