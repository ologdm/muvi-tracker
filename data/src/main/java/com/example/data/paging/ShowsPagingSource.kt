package com.example.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.types.ShowsType
import com.example.data.api.TraktApi
import com.example.data.dto.show.explore.toDomain
import com.example.data.dto.show.toDomain
import com.example.domain.model.base.ShowBase
import java.util.concurrent.CancellationException

class ShowsPagingSource(
    private val feedCategory: ShowsType,
    private val traktApi: TraktApi
) : PagingSource<Int, ShowBase>() {


    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ShowBase> {

        val currentPage = params.key ?: 1

        return try {
            val response = when (feedCategory) {
                ShowsType.Popular -> traktApi.getPopularShowsPage(currentPage, params.loadSize)
                    .map { it.toDomain() }

                ShowsType.Watched -> traktApi.getWatchedShowsPage(currentPage, params.loadSize)
                    .map { it.toDomain() }

                ShowsType.Favorited -> traktApi.getFavoritedShowsPage(currentPage, params.loadSize)
                    .map { it.toDomain() }

                ShowsType.ComingSoon -> traktApi.getAnticipatedShowsPage(currentPage, params.loadSize)
                    .map { it.toDomain() }
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
        state: PagingState<Int, ShowBase>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }


}