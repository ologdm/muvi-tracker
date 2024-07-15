package com.example.muvitracker.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.SearchResult
import com.example.muvitracker.domain.repo.SearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo
) : ViewModel() {

    // coroutines TODO
    val searchLivedataState = MutableLiveData<List<SearchResult>>()


    fun updateSearch(text: String) {
        viewModelScope.launch {
            searchLivedataState.value = searchRepository.getNetworkResultTest(text)
        }
    }



}


//private val searchLivedata = MutableLiveData("")
//
//// update text
//fun updateSearch(text: String) {
//    searchLivedata.value = text
//}
//
//
//// create another livedata, 2nd is updated every time 1st changes
//fun state() = searchLivedata
//    .switchMap { queryText ->
//        searchRepository.getNetworkResult(queryText)
//    }
//
//fun state1(): LiveData<List<SearchResult>> {
//    return searchLivedata.switchMap { queryText ->
//        searchRepository.getNetworkResult(queryText)
//    }
//}