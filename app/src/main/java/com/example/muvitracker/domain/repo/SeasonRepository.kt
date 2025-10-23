package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow


interface SeasonRepository {

    fun getAllSeasonsFlow(
        showIds: Ids
    ): Flow<IoResponse<List<SeasonExtended>>>


    fun getSingleSeasonFlow(
        showId: Int,
        seasonNr: Int
    ): Flow<SeasonExtended>


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