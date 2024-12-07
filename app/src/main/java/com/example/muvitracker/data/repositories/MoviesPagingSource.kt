package com.example.muvitracker.data.repositories

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.R
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.domain.model.base.Movie
import java.util.concurrent.CancellationException
import javax.inject.Singleton

// PagingSource<key,resultValue> - due metodi da implementare
//   1. getRefreshKey
//   2. load

// Result ->
//   - LoadResult.Page - if the result was successful.
//   - LoadResult.Error -  in case of error.
//   - LoadResult.Invalid -  if the PagingSource should be invalidated because it can no longer guarantee the integrity of its results.


@Singleton
class MoviesPagingSource(
    val context: Context,
    private val feedCategory: String, // feedCategory -> solo indicative, le chiamate sono implementate separatamente
    private val traktApi: TraktApi
) : PagingSource<Int, Movie>() {


    // parameters - (currentPage, loadSize)
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {
        val currentPage = params.key ?: 1
        return try {
            val response = when (feedCategory) {
                context.getString(R.string.popular) -> traktApi.getPopularMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                context.getString(R.string.watched) -> traktApi.getWatchedMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                context.getString(R.string.favorited) -> traktApi.getFavoritedMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                context.getString(R.string.anticipated) -> traktApi.getAnticipatedMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                else -> emptyList()
            }

            LoadResult.Page(  // #1
                data = response, // response from network
                prevKey = if (currentPage == 1) null else currentPage - 1, // standard
                nextKey = if (response.isEmpty()) null else currentPage + 1 // standard
            )
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            LoadResult.Error(ex)  // #2
        }
    }


    // OK - default implementation
    override fun getRefreshKey(
        state: PagingState<Int, Movie>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }


}