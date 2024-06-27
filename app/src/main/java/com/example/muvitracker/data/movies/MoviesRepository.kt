package com.example.muvitracker.data.movies


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.data.dto.base.toDomain
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.data.startNetworkCall
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.IoResponse2
import com.example.muvitracker.utils.concat
import com.example.muvitracker.utils.ioMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MoviesRepository @Inject constructor(
    private val api: TraktApi,
    private val moviesDS: MoviesDS
) : MoviesRepo {

    //coroutines ####################################################################
    override
    fun getPopularMoviesFLow(): Flow<IoResponse2<List<Movie>>> {
        return moviesDS.getPopularStoreStream().map { response ->
            response.ioMapper {
                it.map {dto->
                    dto.toDomain() }
            }
        }
    }


    override
    fun getBoxoMoviesFLow(): Flow<IoResponse2<List<Movie>>> {
        return moviesDS.getBoxoStoreStream().map { response ->
           println("XXX REPO BOXOFUN FLOW CHECK")
            response.ioMapper {
                it.map {dto->
                    dto.toDomain() }
            }
        }
    }


}







