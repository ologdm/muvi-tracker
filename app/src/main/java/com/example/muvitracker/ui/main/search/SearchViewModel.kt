package com.example.muvitracker.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.muvitracker.data.search.SearchRepository

class SearchViewModel() : ViewModel() {

    private val searchLivedata = MutableLiveData("")

    fun state() = searchLivedata
        .switchMap { queryText ->
            SearchRepository.getNetworkResult(queryText)
        }

    fun updateSearch(text: String) {
        searchLivedata.value = text
    }

}

