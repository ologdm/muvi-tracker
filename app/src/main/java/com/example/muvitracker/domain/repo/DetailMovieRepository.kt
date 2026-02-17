package com.example.muvitracker.domain.repo


import com.example.muvitracker.data.database.entities.MovieEntity
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.model.Provider
import com.example.muvitracker.domain.model.base.MovieBase

import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow

interface DetailMovieRepository {

    fun getSingleDetailMovieFlow(movieIds: Ids): Flow<IoResponse<Movie>>

    fun getDetailListFlow(): Flow<List<MovieEntity?>>

    suspend fun getRelatedMovies(movieId: Int): IoResponse<List<MovieBase>>

    suspend fun getMovieCast(movieId: Int): IoResponse<CastAndCrew> // contiene List<CastMember> -> personggi film

    suspend fun getMovieProviders(movieId: Int): IoResponse<List<Provider>>


}