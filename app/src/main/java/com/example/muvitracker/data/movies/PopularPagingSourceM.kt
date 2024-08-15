package com.example.muvitracker.data.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.basedto.toDomain
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


// TODO wrapper for generic paging source construction (),
//      param - traktApi.getBoxoMovies(currentPage, params.loadSize)

@Singleton
class PopularPagingSourceM(
    private val traktApi: TraktApi,
//    private val popularDao: PopularDao
) : PagingSource<Int, Movie>() {


    // parameters - (currentPage, loadSize)
    // TODO  - caricare nel fetcher (store)
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {

        val currentPage = params.key ?: 1
        return try {
            val response = traktApi.getPopularMovies(currentPage, params.loadSize)
            LoadResult.Page(  // #1
                data = response.map { dto ->
                    dto.toDomain()
                }, // response from network
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