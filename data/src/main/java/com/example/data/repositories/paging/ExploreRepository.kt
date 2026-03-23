package com.example.data.repositories.paging

import androidx.paging.PagingData
import com.example.data.TraktApi
import com.example.domain.MovieType
import com.example.domain.model.Movie
import com.example.domain.repo.ExploreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExploreRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
) : ExploreRepository {

    // movies, shows, search

    override fun getPopularMovies(type: MovieType): Flow<PagingData<Movie>> {
        TODO("Not yet implemented")
    }


}


// TODO dopo
//    // explore movies - for paging
//    // spostare su data
//    suspend fun getPopularMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getBoxoMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getWatchedMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getFavoritedMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getAnticipatedMovies(page: Int, pageSize: Int): List<MovieBase>