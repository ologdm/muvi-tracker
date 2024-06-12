package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.IoResponse

interface DetailRepo {

    fun getDetailMovie(movieId: Int): LiveData<IoResponse<DetailMovie?>>

}