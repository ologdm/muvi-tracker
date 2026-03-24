package com.example.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.TraktApi
import com.example.data.repositories.paging.MoviesPagingSource
import com.example.data.repositories.paging.ShowsPagingSource
import com.example.domain.MovieType
import com.example.domain.ShowsType
import com.example.domain.model.base.MovieBase
import com.example.domain.model.base.ShowBase
import com.example.domain.repo.ExploreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExploreRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
) : ExploreRepository {

    // movies, shows, search

    // TODO OK -> flow passato a viewmodel
    override fun getMoviesByType(type: MovieType): Flow<PagingData<MovieBase>> {
        // fare pager, che usa paging source
        return Pager(
            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
            pagingSourceFactory = { MoviesPagingSource(type, traktApi) }
        ).flow
    }


    override fun getShowsByType(type: ShowsType): Flow<PagingData<ShowBase>> {
        return Pager(
            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
            pagingSourceFactory = { ShowsPagingSource(type, traktApi) }
        ).flow
    }
}


//dopo
//    // explore movies - for paging
//    // spostare su data
//    suspend fun getPopularMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getBoxoMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getWatchedMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getFavoritedMovies(page: Int, pageSize: Int): List<MovieBase>
//    suspend fun getAnticipatedMovies(page: Int, pageSize: Int): List<MovieBase>