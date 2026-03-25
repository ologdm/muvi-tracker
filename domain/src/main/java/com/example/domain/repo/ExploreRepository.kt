package com.example.domain.repo

import androidx.paging.PagingData
import com.example.domain.types.MovieType
import com.example.domain.types.ShowsType
import com.example.domain.model.base.MovieBase
import com.example.domain.model.base.ShowBase
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {

    fun getMoviesByType(type: MovieType): Flow<PagingData<MovieBase>>

    fun getShowsByType(type: ShowsType): Flow<PagingData<ShowBase>>

}