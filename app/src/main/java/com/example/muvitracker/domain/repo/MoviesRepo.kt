package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow

interface MoviesRepo {
    fun getPopularMoviesFLow(): Flow<IoResponse2<List<Movie>>>
    fun getBoxoMoviesFLow(): Flow<IoResponse2<List<Movie>>>
}