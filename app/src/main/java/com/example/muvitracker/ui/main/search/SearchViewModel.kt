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

    val searchState = MutableLiveData<List<SearchResult>>()



    fun updateSearch(typeFilter: String, text: String) {
        viewModelScope.launch {
            searchState.value = searchRepository.getNetworkResult(typeFilter,text)
        }
    }


}
