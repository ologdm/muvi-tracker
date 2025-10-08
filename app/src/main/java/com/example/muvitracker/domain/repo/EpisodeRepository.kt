package com.example.muvitracker.domain.repo

import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    fun getSeasonAllEpisodesFlow(
        showId: Int, seasonNr: Int
    ): Flow<IoResponse<List<EpisodeExtended>>>

    fun getSingleEpisode(
        showId: Int, seasonNr: Int, episodeNr: Int
    ): Flow<EpisodeExtended?>

    suspend fun toggleSingleWatchedEpisode(
        showId: Int, seasonNr: Int, episodeNr: Int
    )

}