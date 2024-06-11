package com.example.muvitracker.data.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.muvitracker.data.dto.toDomain
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.utils.IoResponse

object SearchRepository {


    fun getNetworkResult(queryText: String): LiveData<List<SearchResult>> {
        val liveData = MutableLiveData<List<SearchResult>>()

        SearchNetworkDS.getServer(queryText = queryText,
            onResponse = { retrofitResponse ->
                if (retrofitResponse is IoResponse.Success) {
                    val sortedList =
                        retrofitResponse.dataValue.sortedByDescending { dto ->
                            dto.score
                        }
                    liveData.value = sortedList.map { dto ->
                        dto.toDomain()
                    }
                }
            })
        return liveData
    }


}

