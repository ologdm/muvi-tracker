package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.domain.model.base.ShowBase
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface DetailShowRepository {

    fun getSingleDetailShowFlow(showId: Int): Flow<IoResponse<Show>>

    suspend fun getRelatedShows(showId: Int): IoResponse<List<ShowBase>>

    suspend fun toggleLikedShow (showId: Int)

    suspend fun checkAndSetWatchedAllShowEpisodes(showIds: Ids)
}