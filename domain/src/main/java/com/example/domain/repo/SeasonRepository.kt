package com.example.domain.repo

import com.example.domain.model.IdsDomain
import com.example.domain.model.Season
import com.example.domain.utils.IoResponse
import kotlinx.coroutines.flow.Flow


interface SeasonRepository {

    fun getAllSeasonsFlow(
        showIds: IdsDomain
    ): Flow<IoResponse<List<Season>>>


    fun getSingleSeasonFlow(
        showId: Int,
        seasonNr: Int
    ): Flow<Season>


    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showIds: IdsDomain,
        seasonNr: Int
    )


    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showIds: IdsDomain,
        seasonNr: Int,
        watchedAllState: Boolean
    )

}