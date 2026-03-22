package com.example.domain.repo


import com.example.domain.utils.IoResponse
import com.example.domain.model.CastAndCrew
import com.example.domain.model.Ids
import com.example.domain.model.Movie
import com.example.domain.model.Provider
import com.example.domain.model.base.MovieBase
import kotlinx.coroutines.flow.Flow

interface DetailMovieRepository {

    fun getSingleDetailMovieFlow(movieIds: Ids): Flow<IoResponse<Movie>>

    // FIXME: fixare il toDomain, serve che la funzione sia nell'imterfaccia?  -> NOTE: -> no, e solo usata nel domain;
//    fun getDetailListFlow(): Flow<List<MovieEntity?>>
//    fun getDetailListFlow(): Flow<List<Movie>>

    suspend fun getRelatedMovies(movieId: Int): IoResponse<List<MovieBase>>

    suspend fun getMovieCast(movieId: Int): IoResponse<CastAndCrew> // contiene List<CastMember> -> personggi film

    suspend fun getMovieProviders(movieId: Int): IoResponse<List<Provider>>


}