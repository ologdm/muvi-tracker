package com.example.domain.repo

import androidx.paging.PagingData
import com.example.domain.types.SearchType
import com.example.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getSearchResult(searchString: String, typeFilter: SearchType): Flow<PagingData<SearchResult>>

}