package com.example.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.TraktApi
import com.example.data.repositories.paging.SearchPagingSource
import com.example.domain.SearchType
import com.example.domain.model.SearchResult
import com.example.domain.repo.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
) : SearchRepository {


    // corretta
    override fun getSearchResult(
        searchString: String,
        typeFilter: SearchType
    ): Flow<PagingData<SearchResult>> {

        val pager = Pager(
            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
            pagingSourceFactory = {
                // TODO passare qua il PagingSource nel factory
                SearchPagingSource(searchString, typeFilter, traktApi)

            }
        )

        return pager.flow // ritorna pager flow
    }


}