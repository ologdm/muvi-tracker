package com.example.muvitracker.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.repositories.paging.SearchPagingSource
import com.example.muvitracker.ui.main.search.SearchFragment.Companion.MOVIE_SHOW_PERSON
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val traktApi: TraktApi,
) : ViewModel() {

    companion object {
        private const val DEBOUNCE_DELAY: Long = 300L
    }


    private val searchQueryFlow = MutableStateFlow("")
    private val filterValueFlow = MutableStateFlow(MOVIE_SHOW_PERSON)


    //  invalidate pernmette di invalidare il factory del paging sourse, aoptrimenti non lo fa
    private val sourceFactory = InvalidatingPagingSourceFactory {
        SearchPagingSource(
            queryValue = searchQueryFlow.value,
            filterValue = filterValueFlow.value,
            traktApi = traktApi
        )
    }

    val statePaging = Pager(
        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = {
            sourceFactory()
        }
    ).flow
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            searchQueryFlow
                .drop(1)
                .debounce(DEBOUNCE_DELAY)
                .collectLatest {
                    sourceFactory.invalidate() // invalido ogni volta, ricreo la factory solo quando immetto valoce nuovo
                }
        }
    }

    fun updateQuery(searchInput: String) {
        searchQueryFlow.value = searchInput
    }

    fun updateFilterValue(filterValue: String) {
        filterValueFlow.value = filterValue
        sourceFactory.invalidate() // invalido ogni volta
    }
}


//    init {
//        viewModelScope.launch {
//            searchQueryFlow
//                .debounce(DEBOUNCE_DELAY)
////                .filter { searchQuery-> searchQuery.isNotBlank() } // not used
//                .combine(filterValueFlow) { queryValue, filterValue ->
//                    queryValue to filterValue   // Pair(string, string)
//                }
//                .collectLatest { (queryValue, filterValue) ->
//                    searchResultFlow.value = searchRepository.getNetworkResult(
//                        searchQuery = queryValue,
//                        typeFilter = filterValue
//                    )
//                }
//        }
//    }


//    val statePaging = combine(
//        searchQueryFlow.debounce(DEBOUNCE_DELAY),
//        filterValueFlow
//    ) { query, type ->
//        Pager(
//            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
//            pagingSourceFactory = {
//                SearchPagingSource(query, type, traktApi)
//            }
//        ).flow
//            .cachedIn(viewModelScope)
//    }