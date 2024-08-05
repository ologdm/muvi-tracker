package com.example.muvitracker.domain.repo

import com.example.muvitracker.data.database.entities.DetailEntity
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.model.base.Movie

import com.example.muvitracker.utils.IoResponse2
import kotlinx.coroutines.flow.Flow

interface MoviesRepo {

    fun getBoxoStoreStream() : Flow<IoResponse2<List<Movie?>>>


}