package com.example.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.TraktApi
import com.example.data.dto.search.toDomain
import com.example.domain.types.SearchType
import com.example.domain.model.SearchResult

// per goni ricerca si aggiorna
class SearchPagingSource(
    private val queryValue: String,
//    private val filterValue: String,
    private val filterValue: SearchType,
    private val traktApi: TraktApi
) : PagingSource<Int, SearchResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        val currentPage = params.key ?: 1

        try {
            val response = traktApi.getSearch(
                typeFilter = filterValue.value,
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