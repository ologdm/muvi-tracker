package com.example.muvitracker.data.repositories

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.R
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.ui.main.allshows.ShowsType
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowsPagingSource @Inject constructor(
    private val context: Context,
    private val feedCategory: ShowsType,
    private val traktApi: TraktApi
) : PagingSource<Int, Show>() {


    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Show> {

        val currentPage = params.key ?: 1
        return try {
            val response = when (feedCategory) {
                ShowsType.Popular -> traktApi.getPopularShows(currentPage, params.loadSize).map { it.toDomain() }
                ShowsType.Watched -> traktApi.getWatchedShows(currentPage, params.loadSize).map { it.toDomain() }
                ShowsType.Favorites -> traktApi.getFavoritedShows(currentPage, params.loadSize).map { it.toDomain() }
                ShowsType.ComingSoon -> traktApi.getAnticipatedShows(currentPage, params.loadSize).map { it.toDomain() }
            }

            LoadResult.Page(  // #1
                data = response,// response from network
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
        state: PagingState<Int, Show>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }


}