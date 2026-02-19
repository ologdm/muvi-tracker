package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.domain.model.base.MovieBase
import com.example.muvitracker.domain.model.base.ShowBase
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import java.security.Provider

interface DetailShowRepository {

    fun getSingleDetailShowFlow(showIds: Ids): Flow<IoResponse<Show>>

    suspend fun getRelatedShows(showId: Int): IoResponse<List<ShowBase>>

    suspend fun getShowCast(movieId: Int): IoResponse<CastAndCrew> // contiene List<CastMember> -> personggi film

    suspend fun checkAndSetWatchedAllShowEpisodes(showIds: Ids)

    suspend fun getShowProviders (showId: Int): IoResponse<List<com.example.muvitracker.domain.model.Provider>>
}