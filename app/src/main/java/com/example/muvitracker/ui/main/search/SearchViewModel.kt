package com.example.muvitracker.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.domain.repo.SearchRepo
import com.example.muvitracker.ui.main.search.SearchFragment.Companion.MOVIE_SHOW_PERSON
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo
) : ViewModel() {

    companion object {
        private const val DEBOUNCE_DELAY: Long = 300L
    }


    // 1) flow search
    private val searchQueryFlow = MutableStateFlow("")

    fun updateQuery(searchInput: String) {
        searchQueryFlow.value = searchInput
    }

    // 2) flow filter
    private val filterValueFlow = MutableStateFlow(MOVIE_SHOW_PERSON)

    fun updateFilterValue(filterValue: String) {
        filterValueFlow.value = filterValue
    }


    init {
        // 4) fai chiamata -> quando dati cambiano, collect new result
        viewModelScope.launch {
            searchQueryFlow
                .debounce(DEBOUNCE_DELAY)
//                .filter { it.isNotBlank() }
                .collectLatest {
                    loadSearchResult(filterValueFlow.first(), it)
                }
        }

    }


    // RISULTATI
    // TODO StarteContainer con result, no internet, ecc
//    val searchResultState1 = MutableLiveData<List<SearchResult>>()
    val searchResultState = MutableStateFlow<List<SearchResult>>(emptyList())

    private fun loadSearchResult(typeFilter: String, text: String) {
        viewModelScope.launch {
            searchResultState.value = searchRepository.getNetworkResult(typeFilter, text)
            println()
        }
    }

}
