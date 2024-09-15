package com.example.muvitracker.data.shows

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.R
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.movies.toDomain
import com.example.muvitracker.data.dto.show.toDomain
import com.example.muvitracker.domain.model.base.Show
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowsPagingSource @Inject constructor(
    private val context: Context,
    private val feedCategory: String,
    private val traktApi: TraktApi
) : PagingSource<Int, Show>() {


    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Show> {

        val currentPage = params.key ?: 1
        return try {
            val response = when (feedCategory) {
                context.getString(R.string.popular) ->
                    traktApi.getPopularShows(currentPage, params.loadSize).map { it.toDomain() }

                context.getString(R.string.watched) ->
                    traktApi.getWatchedShows(currentPage, params.loadSize).map { it.toDomain() }

                context.getString(R.string.favorited) ->
                    traktApi.getFavoritedShows(currentPage, params.loadSize).map { it.toDomain() }

                context.getString(R.string.anticipated) ->
                    traktApi.getAnticipatedShows(currentPage, params.loadSize).map { it.toDomain() }

                else -> emptyList()
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