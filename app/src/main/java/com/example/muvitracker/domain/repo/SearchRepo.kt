package com.example.muvitracker.domain.repo

import com.example.muvitracker.domain.model.SearchResult

interface SearchRepo {
    suspend fun getNetworkResult(typeFilter: String, searchQuery: String): List<SearchResult>
}