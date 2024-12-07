package com.example.muvitracker.domain.repo

import com.example.muvitracker.domain.model.SearchResult

// TODO delete
interface SearchRepo {
    suspend fun getNetworkResult(
        typeFilter: String,
        searchQuery: String,
        page: Int,
    ): List<SearchResult>
}