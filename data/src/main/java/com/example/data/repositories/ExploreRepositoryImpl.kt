package com.example.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.TraktApi
import com.example.data.paging.MoviesPagingSource
import com.example.data.paging.ShowsPagingSource
import com.example.domain.types.MovieType
import com.example.domain.types.ShowsType
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