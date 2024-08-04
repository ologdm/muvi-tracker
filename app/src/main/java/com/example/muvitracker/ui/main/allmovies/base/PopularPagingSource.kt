package com.example.muvitracker.ui.main.allmovies.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.PopularDao
import com.example.muvitracker.data.database.entities.PopularMovieEntity
import com.example.muvitracker.data.dto.basedto.MovieDto
import com.example.muvitracker.data.dto.basedto.toEntity
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.CancellationException

// PagingSource<key,resultValue> - due metodi da implementare
//   1. getRefreshKey
//   2. load

// Result ->
//   - LoadResult.Page - if the result was successful.
//   - LoadResult.Error -  in case of error.
//   - LoadResult.Invalid -  if the PagingSource should be invalidated because it can no longer guarantee the integrity of its results.


class PopularPagingSource(
    private val traktApi: TraktApi,
//    private val popularDao: PopularDao
) : PagingSource<Int, MovieDto>() {


    // carica dati , paramentri - (currentPage, loadSize)
    // TODO  - caricare nel fetcher (store)
    override suspend fun load(
        params: LoadParams<Int> // passo sul momento
    ): LoadResult<Int, MovieDto> {
        // Pagina corrente, inizia da 1 se è nullo
        val currentPage = params.key ?: 1 // definisco
        return try {
            val response = traktApi.getPopularMoviesPaging(currentPage, params.loadSize)
//            popularDao.insertList(response.map { it.toEntity() }) TODO
//            val data = popularDao.readAll().firstOrNull().orEmpty() TODO

            LoadResult.Page(  // #1
                data = response, // response from network
                prevKey = if (currentPage == 1) null else currentPage - 1, // standard
                nextKey = if (response.isEmpty()) null else currentPage + 1 // standard
            )
        } catch (ex: CancellationException) {
            throw ex // devo metterla perche catturando le exception non vado a fare il throw automatico, cioe cancellazione coroutines
        } catch (ex: Exception) {
            LoadResult.Error(ex)  // #2
        }
    }


    /*
    Next we need to implement getRefreshKey(). This method is called when the Paging library
    needs to reload items for the UI because the data in its backing PagingSource has changed.
    This situation where the underlying data for a PagingSource has changed and needs to be updated
    in the UI is called invalidation. When invalidated, the Paging Library creates a new PagingSource
    to reload the data, and informs the UI by emitting new PagingData. We'll learn more about invalidation
    in a later section.
    Invalidation in the paging library occurs for one of two reasons:
     - You called refresh() on the PagingAdapter.
     - You called invalidate() on the PagingSource.
     */
    // OK, implementazione default???  eugi
    override fun getRefreshKey(
        state: PagingState<Int, MovieDto>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }


}