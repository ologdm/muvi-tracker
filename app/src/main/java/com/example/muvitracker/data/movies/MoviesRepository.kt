package com.example.muvitracker.data.movies


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.dropbox.android.external.store4.StoreResponse
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
    private val moviesLocalDS: MoviesLocalDS,
    private val api: TraktApi,
    private val moviesDScoroutines: MoviesDScoroutines
) : MoviesRepo {


    // POPULAR ################################################################################

    override fun getPopularMovies(): LiveData<IoResponse<List<Movie>>> {
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


    override fun getPopularCache(): List<Movie> {
        return moviesLocalDS.getPopularLivedataList().value.orEmpty()
    }


    // BOXOFFICE #######################################################################################
    override fun getBoxoMovies(): LiveData<IoResponse<List<Movie>>> {
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


    override fun getBoxoCache(): List<Movie> {
        return moviesLocalDS.getBoxoLivedataList().value.orEmpty()
    }


    // IoResponse eliminato

    //coroutines ####################################################################
    // OK
    override
    fun getPopularMoviesFLow(): Flow<IoResponse2<List<Movie>>> {
        return moviesDScoroutines.getStoreStreamFlow().map { response ->
            response.ioMapper {
                it.map {dto->
                    dto.toDomain() }
            }
        }
    }
}


// 2 ------------------------
//            when (response) {
//                is StoreResponse.Data -> {
//                    StoreResponse.Data(
//                        response.value.map { it.toDomain() })
//                }
//                is StoreResponse.Loading -> {
//                    StoreResponse.Loading(response.type)
//                }
//                is StoreResponse.Error -> {
//                    StoreResponse.Error(response.)
//                }
//
//                is StoreResponse.NoNewData -> TODO()
//            }





