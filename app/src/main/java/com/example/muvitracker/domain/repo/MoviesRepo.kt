package com.example.muvitracker.domain.repo

import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow


interface MoviesRepo {
    fun getBoxoStoreStream() : Flow<IoResponse<List<Movie>>>
}