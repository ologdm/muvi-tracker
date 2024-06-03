package com.example.muvitracker.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.data.dto.SearchDto
import com.example.muvitracker.data.search.SearRepo
import com.example.muvitracker.utils.IoResponse

class SearchViewModel() : ViewModel() {

    val searchList = MutableLiveData<List<SearchDto>>()


    fun loadNetworkResult(queryText: String) {
        SearRepo.getNetworkResult(
            queryText,
            onResponse = { response ->
                if (response is IoResponse.Success) {
                    searchList.value = response.dataValue
                }
            })
    }

}