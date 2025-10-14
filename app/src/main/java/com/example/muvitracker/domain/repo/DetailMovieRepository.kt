package com.example.muvitracker.domain.repo

import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.DetailMovieEntityTmdb
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie

import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface DetailMovieRepository {

    fun getSingleDetailMovieFlow(id: Int): Flow<IoResponse<DetailMovie>>

    fun getDetailListFlow(): Flow<List<DetailMovieEntity?>>

    suspend fun getRelatedMovies(movieId: Int): IoResponse<List<Movie>>

    // TODO tesrt fun
    fun getTmdbFlowTest(key: Int): Flow<StoreResponse<DetailMovieEntityTmdb>>
    fun getTmdbFlowTest2(key: Int): Flow<DetailMovieEntityTmdb>
    suspend fun getTmdbOnce(key: Int): DetailMovieEntityTmdb?

}