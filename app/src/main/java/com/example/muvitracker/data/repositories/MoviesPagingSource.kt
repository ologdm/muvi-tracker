package com.example.muvitracker.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.movie.toDomain
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.ui.main.allmovies.MovieType
import com.example.muvitracker.ui.main.allshows.ShowsType
import java.util.concurrent.CancellationException

// PagingSource<key,resultValue> - due metodi da implementare
//   1. getRefreshKey
//   2. load

// Result ->
//   - LoadResult.Page - if the result was successful.
//   - LoadResult.Error -  in case of error.
//   - LoadResult.Invalid -  if the PagingSource should be invalidated because it can no longer guarantee the integrity of its results.


class MoviesPagingSource(
    private val feedCategory: MovieType,
    private val traktApi: TraktApi
) : PagingSource<Int, Movie>() {


    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {
        val currentPage = params.key ?: 1
        return try {
            val response = when (feedCategory) {
                MovieType.Popular -> traktApi.getPopularMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                MovieType.BoxOffice -> traktApi.getBoxoMovies() // only 10 results
                    .map { it.toDomain() }

                MovieType.Watched -> traktApi.getWatchedMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                MovieType.Favorited -> traktApi.getFavoritedMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }

                MovieType.ComingSoon -> traktApi.getAnticipatedMovies(currentPage, params.loadSize)
                    .map { it.toDomain() }
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