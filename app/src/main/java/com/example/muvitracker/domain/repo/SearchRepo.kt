package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.example.muvitracker.domain.model.SearchResult

interface SearchRepo {

    suspend fun getNetworkResult(queryText: String): List<SearchResult>
}