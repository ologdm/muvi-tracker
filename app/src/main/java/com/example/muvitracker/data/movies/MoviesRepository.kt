package com.example.muvitracker.data.movies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.data.dto.base.toDomain
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.data.startNetworkCall
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.concat
import com.example.muvitracker.utils.ioMapper


class MoviesRepository
private constructor(
    private val context: Context
) {

    private val moviesLocalDS = MoviesLocalDS.getInstance(context)
    private val api = com.example.muvitracker.data.RetrofitUtils.traktApi


    // POPULAR ################################################################################

    fun getPopularMovies(): LiveData<IoResponse<List<Movie>>> {
        // 1
        val localLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        val localMovies = moviesLocalDS.getPopularLivedataList().value.orEmpty()
        localLivedata.value = IoResponse.success(localMovies)

        // 2
        val networkLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        api.getPopularMovies().startNetworkCall { retrofitResponse ->
            val ioMapper = retrofitResponse.ioMapper { listDto ->
                val mapperList = listDto.map { it.toDomain() }
                moviesLocalDS.savePopularInLocal(mapperList) // SALVA IN LOCALE
                mapperList
            }
            networkLivedata.value = ioMapper
        }
        // 3
        return concat(
            localLivedata,
            networkLivedata
        ).distinctUntilChanged()
    }

    fun getPopularCache(): List<Movie> {
        return moviesLocalDS.getPopularLivedataList().value.orEmpty()
    }


    // BOXOFFICE #######################################################################################

    fun getBoxoMovies(): LiveData<IoResponse<List<Movie>>> {
        val localLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        val localMovies = moviesLocalDS.getBoxoLivedataList().value.orEmpty()
        localLivedata.value = IoResponse.success(localMovies)

        val networkLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        api.getBoxoMovies().startNetworkCall { retrofitResponse ->
            val ioMapper = retrofitResponse.ioMapper { listDto ->
                val mapperList = listDto.map { it.toDomain() }
                moviesLocalDS.saveBoxoInLocal(mapperList) // save on db
                mapperList
            }
            networkLivedata.value = ioMapper
        }

        return concat(
            localLivedata,
            networkLivedata
        ).distinctUntilChanged()
    }


    fun getBoxoCache(): List<Movie> {
        return moviesLocalDS.getBoxoLivedataList().value.orEmpty()
    }


    companion object {
        private var instance: MoviesRepository? = null
        fun getInstance(context: Context): MoviesRepository {
            if (instance == null) {
                instance = MoviesRepository(context)
            }
            return instance!!
        }
    }
}
