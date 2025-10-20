package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow


interface SeasonRepository {

    // 1
//    fun getAllSeasonsFlow(showId: Int): Flow<IoResponse<List<SeasonExtended>>>
    fun getAllSeasonsFlow(showIds: Ids): Flow<IoResponse<List<SeasonExtended>>>

    // 2
    fun getSingleSeasonFlow(showId: Int, seasonNr: Int): Flow<SeasonExtended>

    // 3
    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int
    )

    // 4
    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int,
        watchedAllState: Boolean
    )

}