package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.Season
import com.example.muvitracker.utils.IoResponse
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