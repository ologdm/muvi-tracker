package com.example.muvitracker.data.search

import com.example.muvitracker.data.dto.SearchDto
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ioMapper

object SearRepo {

    // TODO fare anche toDomain

    fun getNetworkResult(queryText: String, onResponse: (IoResponse<List<SearchDto>>) -> Unit) {

        SearNetworkDS.getServer(
            queryText = queryText,
            onResponse = { retrofitResponse ->
                val mappedIo = retrofitResponse.ioMapper { list ->
                    val sortedList = list.sortedByDescending { it.score }
                    sortedList
                }
                onResponse(mappedIo)
            })
    }
}