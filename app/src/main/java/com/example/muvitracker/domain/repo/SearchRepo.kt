package com.example.muvitracker.domain.repo

import androidx.lifecycle.LiveData
import com.example.muvitracker.domain.model.SearchResult

interface SearchRepo {

//    fun getNetworkResult(queryText: String): LiveData<List<SearchResult>>

    suspend fun getNetworkResultTest(queryText: String): List<SearchResult>
}