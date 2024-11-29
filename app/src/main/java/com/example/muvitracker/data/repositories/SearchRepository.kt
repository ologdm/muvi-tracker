package com.example.muvitracker.data.repositories

import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.domain.repo.SearchRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class SearchRepository @Inject constructor(
    private val traktApi: TraktApi
) : SearchRepo {

    override
    suspend fun getNetworkResult(typeFilter: String, searchQuery: String): List<SearchResult> {
        try {
            return traktApi.getSearch(typeFilter, searchQuery)
//                .sortedByDescending { dto -> dto.score } // don't use
                .map { dto -> dto.toDomain() }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
        return emptyList()
    }

}

