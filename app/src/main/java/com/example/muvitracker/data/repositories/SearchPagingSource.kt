package com.example.muvitracker.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.domain.model.SearchResult

class SearchPagingSource(
    private val queryValue: String,
    private val filterValue: String,
    private val traktApi: TraktApi
) : PagingSource<Int, SearchResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        val currentPage = params.key ?: 1

        try {
            val response = traktApi.getSearch(
                typeFilter = filterValue,
                query = queryValue,
                page = currentPage,
            )
            return LoadResult.Page(
                data = response.map { it.toDomain() },
                prevKey = null,
                nextKey = if (response.isEmpty()) null else currentPage + 1 // standard
            )
        } catch (ex: Throwable) {
            return LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return null
    }

}