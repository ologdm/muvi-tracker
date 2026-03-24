package com.example.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.SearchType
import com.example.domain.repo.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
//    private val traktApi: TraktApi,
    // TODO repo
    private val searchRepo: SearchRepository // TODO provides OK
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("") // string ok

    //    private val filterValueFlow = MutableStateFlow(MOVIE_SHOW_PERSON) // old string
    private val filterValueFlow =
        MutableStateFlow(SearchType.MovieShowPerson) // passiamo il type al modulo data

    // OLD
//    val statePaging = Pager(
//        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
//        pagingSourceFactory = {
//            sourceFactory()
//        }
//    ).flow
//        .cachedIn(viewModelScope)

    // TODO NEW
    //  CHECK INVALIDAZIONE
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


    // TODO eliminare
    //  invalidate pernmette di invalidare il factory del paging sourse, aoptrimenti non lo fa
//    private val sourceFactory = InvalidatingPagingSourceFactory {
    // old
//        SearchPagingSource(
//            queryValue = searchQueryFlow.value,
//            filterValue = filterValueFlow.value,
//            traktApi = traktApi
//        )
    // new TODO devo passargli lo stesso tipo
//        searchRepo.getSearchResult(
//            searchString = searchQueryFlow.value,
//            typeFilter = filterValueFlow.value
//        )
//    }

    // NOTE: gestione debounce query OK
//    init {
//        viewModelScope.launch {
//
//            searchQueryFlow
//                .drop(1)
//                .debounce(DEBOUNCE_DELAY)
//                .collectLatest {
//                    sourceFactory.invalidate() // invalido ogni volta, ricreo la factory solo quando immetto valoce nuovo
//                }
//        }
//    }


    // NOTE: aggiornamenti flow di query e type OK
    fun updateQuery(searchInput: String) {
        searchQueryFlow.value = searchInput
    }

    fun updateFilterValue(filterValue: SearchType) {
        filterValueFlow.value = filterValue // ok SearchType
//        sourceFactory.invalidate() // invalido ogni volta; non serve piu
    }


    companion object {
        private const val DEBOUNCE_DELAY: Long = 300L
    }
}
