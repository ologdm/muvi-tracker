package com.example.muvitracker.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.muvitracker.domain.repo.SearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo
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

