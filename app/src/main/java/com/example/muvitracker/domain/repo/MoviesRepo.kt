package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.utils.IoResponse

interface MoviesRepo {

    fun getPopularMovies(): LiveData<IoResponse<List<Movie>>>

    fun getPopularCache(): List<Movie>

    fun getBoxoMovies(): LiveData<IoResponse<List<Movie>>>

    fun getBoxoCache(): List<Movie>

}