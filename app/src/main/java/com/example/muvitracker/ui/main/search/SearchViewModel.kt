package com.example.muvitracker.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.domain.repo.SearchRepo
import com.example.muvitracker.ui.main.search.SearchFragment.Companion.MOVIE_SHOW_PERSON
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo
) : ViewModel() {

    companion object {
        private const val DEBOUNCE_DELAY: Long = 300L
    }


    private val searchQueryFlow = MutableStateFlow("")
    private val filterValueFlow = MutableStateFlow(MOVIE_SHOW_PERSON)
    val searchResultFlow = MutableStateFlow<List<SearchResult>>(emptyList())


    fun updateQuery(searchInput: String) {
        searchQueryFlow.value = searchInput
    }

    fun updateFilterValue(filterValue: String) {
        filterValueFlow.value = filterValue
    }


    init {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(DEBOUNCE_DELAY)
//                .filter { searchQuery-> searchQuery.isNotBlank() } // not used
                .combine(filterValueFlow) { queryValue, filterValue ->
                    queryValue to filterValue
                }
                .collectLatest { (queryValue, filterValue) ->
                    loadSearchResult(typeFilter = filterValue, searchQuery = queryValue)
                }
        }
    }


    private fun loadSearchResult(typeFilter: String, searchQuery: String) {
        viewModelScope.launch {
            searchResultFlow.value = searchRepository.getNetworkResult(typeFilter, searchQuery)

            // TODO: StarteContainer con result, no internet, ecc
        }
    }

}
