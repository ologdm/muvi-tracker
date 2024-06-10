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


    // TODO
    fun getPopularMovies(): LiveData<IoResponse<List<Movie>>> {
        // 1
        val localLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        val localMovies = moviesLocalDS.getPopularLivedataList().value.orEmpty()
        localLivedata.value = IoResponse.success(localMovies)

        // 2 ZZ
        val networkLivedata = MutableLiveData<IoResponse<List<Movie>>>()
        api.getPopularMovies().startNetworkCall { retrofitResponse ->
            val ioMapper = retrofitResponse.ioMapper { listDto ->
                val mapperList = listDto.map { it.toDomain() }
                moviesLocalDS.savePopularInLocal(mapperList) // SALVA IN LOCALE
                mapperList
            }
            networkLivedata.value = ioMapper // AGGIORNO LIVEDATA OK
            println()
        }


        // 3. ok funziona?????????
        return concat(
            localLivedata,
            networkLivedata
        ).distinctUntilChanged()
        // bloccca aggiornamento es? no || se risposta server == db, legge da db, non si riaggiorna IoResponse
    }


    // TODO
    fun getPopularCache(): List<Movie> {
        return moviesLocalDS.getPopularLivedataList().value.orEmpty()
    }


    // BOXOFFICE #######################################################################################

    fun getBoxoMovies(onResponse: (IoResponse<List<Movie>>) -> Unit) {
        onResponse(getBoxoCache())

        api.getBoxofficeMovies().startNetworkCall { retrofitResponse ->
            val mappedIo = retrofitResponse.ioMapper { list ->
                moviesLocalDS.saveBoxoInLocal(list) // salva in locale dto // TODO check

                val mappedList = list.map { dto ->
                    dto.toDomain()
                }
                mappedList
            }
            onResponse(mappedIo)
        }
    }


    private fun getBoxoCache(): IoResponse.Success<List<Movie>> {
        val mapperList = moviesLocalDS.loadBoxoFromShared().map { boxoDto ->
            boxoDto.toDomain()
        }
        return IoResponse.Success(mapperList)
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


// before test
// ZZ TODO
//fun getPopularMovies(onResponse: (IoResponse<List<Movie>>) -> Unit) {
//    // 1. fetch data from cache
//    val cachedData = getPopularCache()
//    onResponse(cachedData)
//    println("XXX SUCCESS CACHE REPOSITORY")
//
//    //2.
//    onResponse(IoResponse.Loading)
//
//    // 3. fetch data from network
//    api.getPopularMovies().startNetworkCall { retrofitResponse ->
//        val mappedIo = retrofitResponse.ioMapper { list ->
//            moviesLocalDS.savePopularInLocal(list) // save to local cache
//            val mappedList = list.map { dto ->
//                dto.toDomain()
//            }
//            mappedList// !!! forma finale che deve avere R (List<MovieModel>), la definisco qua
//        }
//        onResponse(mappedIo) // forma corretta già definita
//    }
//}
//
//// ZZ TODO
//fun getPopularCache(): IoResponse.Success<List<Movie>> {
//    // !! non ho un IoResponse da trasformare, ma solo da creare uno nuovo
//    val mappedList = moviesLocalDS.loadPopularFromShared().map { popuDto ->
//        popuDto.toDomain()
//    }
//    return IoResponse.Success(mappedList)
//}


// old
//    fun getPopularMovies(onResponse: (IoResponse<List<MovieItem>>) -> Unit) {
//        onResponse(getPopularCache())
//
//        api.getPopularMovies().startNetworkCall { retrofitResponse ->
//            val mappedIo = retrofitResponse.ioMapper { list ->
//                moviesLocalDS.savePopularInLocal(list) // salva in locale
//                val mappedList = list.map { dto ->
//                    dto.toDomain()
//                }
//                mappedList// !!! forma finale che deve avere R (List<MovieModel>), la definisco qua
//            }
//            onResponse(mappedIo) // forma corretta già definita
//        }
//    }







