package com.example.muvitracker.data

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
    suspend fun getNetworkResult(typeFilter: String, queryText: String): List<SearchResult> {
        try {
            return traktApi.getSearch(typeFilter, queryText)
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

