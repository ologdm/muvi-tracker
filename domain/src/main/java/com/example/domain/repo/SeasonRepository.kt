package com.example.domain.repo

import com.example.domain.model.Ids
import com.example.domain.model.Season
import com.example.domain.IoResponse
import kotlinx.coroutines.flow.Flow


interface SeasonRepository {

    fun getAllSeasonsFlow(
        showIds: Ids
    ): Flow<IoResponse<List<Season>>>


    fun getSingleSeasonFlow(
        showId: Int,
        seasonNr: Int
    ): Flow<Season>


    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showIds: Ids,
        seasonNr: Int
    )


    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showIds: Ids,
        seasonNr: Int,
        watchedAllState: Boolean
    )

}