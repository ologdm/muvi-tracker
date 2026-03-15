package com.example.domain.repo


import com.example.domain.model.CastAndCrew
import com.example.domain.model.Ids
import com.example.domain.model.Movie
import com.example.domain.model.Provider
import com.example.domain.model.base.MovieBase
import com.example.domain.utils.IoResponse
//import com.example.muvitracker.dataX.databaseX.entities.MovieEntity

import kotlinx.coroutines.flow.Flow

interface DetailMovieRepository {

    fun getSingleDetailMovieFlow(movieIds: Ids): Flow<IoResponse<Movie>>

//    fun getDetailListFlow(): Flow<List<MovieEntity?>> // FIXME: fixare il toDomain, serve che la funzione sia nell'imterfaccia?

    suspend fun getRelatedMovies(movieId: Int): IoResponse<List<MovieBase>>

    suspend fun getMovieCast(movieId: Int): IoResponse<CastAndCrew> // contiene List<CastMember> -> personggi film

    suspend fun getMovieProviders(movieId: Int): IoResponse<List<Provider>>


}