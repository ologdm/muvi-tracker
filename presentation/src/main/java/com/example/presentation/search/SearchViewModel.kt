package com.example.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.types.SearchType
import com.example.domain.repo.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

// TODO:
//  - check invalidazione
//  - start searchtype - solo in un posto per tutti gli elementi che lo usano

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepo: SearchRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    private val filterValueFlow =
        MutableStateFlow(SearchType.MovieShowPerson)

    // TODO 1.2.+ - NEW LOGIC - CHECK INVALIDAZIONE
    val statePaging = searchQueryFlow // flow string
        .debounce(DEBOUNCE_DELAY)
        .combine(filterValueFlow) { query, type ->
            query to type // crea un flow<pair<String,Type>>
        }
        .flatMapLatest { (query, type) -> // crea flow<pagingData<SearchResult>>
            if (query.isBlank()) { // crea un paging data vuoto
                flowOf(PagingData.empty())
            } else { // cerca risultato
                searchRepo.getSearchResult(searchString = query, typeFilter = type)
            }
        }
        .cachedIn(viewModelScope)


    // aggiornamenti flow di query e type --------------------------------
    fun updateQuery(searchInput: String) {
        searchQueryFlow.value = searchInput
    }

    fun updateFilterValue(filterValue: SearchType) {
        filterValueFlow.value = filterValue
    }


    companion object {
        private const val DEBOUNCE_DELAY: Long = 300L
    }
}
