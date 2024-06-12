package com.example.muvitracker.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.muvitracker.data.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val searchLivedata = MutableLiveData("")

    fun state() = searchLivedata
        .switchMap { queryText ->
            searchRepository.getNetworkResult(queryText)
        }

    fun updateSearch(text: String) {
        searchLivedata.value = text
    }

}

