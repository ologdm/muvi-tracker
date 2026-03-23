package com.example.domain.repo

import androidx.paging.PagingData
import com.example.domain.MovieType
import com.example.domain.model.Movie
import com.example.domain.model.base.MovieBase
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {

    fun getPopularMovies(type: MovieType): Flow<PagingData<Movie>>

}