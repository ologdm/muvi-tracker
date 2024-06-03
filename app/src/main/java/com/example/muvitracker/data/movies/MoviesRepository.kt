package com.example.muvitracker.data.movies

import android.content.Context
import com.example.muvitracker.data.RetrofitUtils
import com.example.muvitracker.domain.model.MovieItem
import com.example.muvitracker.data.dto.base.toDomain
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.data.startNetworkCall
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper


class MoviesRepository
private constructor(
    private val context: Context
) {

    private val moviesLocalDS = MoviesLocalDS.getInstance(context)

    private val api = com.example.muvitracker.data.RetrofitUtils.traktApi
    // TODO -vmodificare con hilt, devo solo pigliare la call


    // POPULAR ----------------------------------
    fun getPopularMovies(onResponse: (IoResponse<List<MovieItem>>) -> Unit) {
        onResponse(getPopularCache())

        api.getPopularMovies().startNetworkCall { retrofitResponse ->
            val mappedIo = retrofitResponse.ioMapper { list ->
                moviesLocalDS.savePopularInLocal(list) // salva in locale
                val mappedList = list.map { dto ->
                    dto.toDomain()
                }
                mappedList// !!! forma finale che deve avere R (List<MovieModel>), la definisco qua
            }
            onResponse(mappedIo) // forma corretta già definita
        }
    }


    private fun getPopularCache(): IoResponse.Success<List<MovieItem>> {
        // !! non ho un IoResponse da trasformare, ma solo da creare uno nuovo
        val mappedList = moviesLocalDS.loadPopularFromLocal().map { popuDto ->
            popuDto.toDomain()
        }
        return IoResponse.Success(mappedList)
    }


    // BOXO ---------------------------------------
    fun getBoxoMovies(onResponse: (IoResponse<List<MovieItem>>) -> Unit) {
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


    private fun getBoxoCache(): IoResponse.Success<List<MovieItem>> {
        val mapperList = moviesLocalDS.loadBoxoFromLocal().map { boxoDto ->
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









