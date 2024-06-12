package com.example.muvitracker.data.movies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.example.muvitracker.data.RetrofitModule
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.data.dto.base.toDomain
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.data.startNetworkCall
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.concat
import com.example.muvitracker.utils.ioMapper
import javax.inject.Inject
import javax.inject.Singleton

// il modulo deve sapere come immplementare il tipo interfaccia - MoviesRepo
// implemento modulo - per testing

@Singleton
class MoviesRepository @Inject constructor(
    private val moviesLocalDS: MoviesLocalDS,
    private val api: TraktApi, // = RetrofitModule.getApi(),
) {

//    private val moviesLocalDS = MoviesLocalDS.getInstance(context)
//    private val api = RetrofitUtils.traktApi


    // POPULAR ################################################################################

    fun getPopularMovies(): LiveData<IoResponse<List<Movie>>> {
        val localLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        val localMovies = moviesLocalDS.getPopularLivedataList().value.orEmpty()
        localLivedata.value = IoResponse.success(localMovies)

        val networkLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        api.getPopularMovies().startNetworkCall { retrofitResponse ->
            val ioMapper = retrofitResponse.ioMapper { listDto ->
                val mapperList = listDto.map { it.toDomain() }
                moviesLocalDS.savePopularInLocal(mapperList) // SALVA IN LOCALE
                mapperList
            }
            networkLivedata.value = ioMapper
        }

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


    //  companion object - non serve vopn Hilt


}
