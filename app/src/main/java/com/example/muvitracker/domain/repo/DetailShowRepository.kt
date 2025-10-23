package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface DetailShowRepository {

    fun getSingleDetailShowFlow(showId: Int): Flow<IoResponse<DetailShow>>

    suspend fun getRelatedShows(showId: Int): IoResponse<List<Show>>

    suspend fun toggleLikedShow (showId: Int)

    suspend fun checkAndSetWatchedAllShowEpisodes(showIds: Ids)
}