package com.example.domain.repo

import com.example.domain.utils.IoResponse
import com.example.domain.model.CastAndCrew
import com.example.domain.model.IdsDomain
import com.example.domain.model.Provider
import com.example.domain.model.Show
import com.example.domain.model.base.ShowBase
import kotlinx.coroutines.flow.Flow

interface DetailShowRepository {

    fun getSingleDetailShowFlow(showIds: IdsDomain): Flow<IoResponse<Show>>

    suspend fun getRelatedShows(showId: Int): IoResponse<List<ShowBase>>

    suspend fun getShowCast(movieId: Int): IoResponse<CastAndCrew> // contiene List<CastMember> -> personggi film

    suspend fun checkAndSetWatchedAllShowEpisodes(showIds: IdsDomain)

    suspend fun getShowProviders (showId: Int): IoResponse<List<Provider>>
}