package com.example.muvitracker.ui.main.allshows

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.repositories.ShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ShowsViewmodel @Inject constructor(
    @ApplicationContext val applicationContext: Context,
    private val traktApi: TraktApi
) : ViewModel() {

    private val _selectedFeed = MutableStateFlow("")
    val selectedFeed: StateFlow<String> = _selectedFeed

    val statePaging = selectedFeed.flatMapLatest {
        Pager(
            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
            pagingSourceFactory = { ShowsPagingSource(applicationContext, it, traktApi) }
        ).flow
            .cachedIn(viewModelScope)
    }


    fun updateSelectedFeed(feedCategory: String) {
        _selectedFeed.value = feedCategory
    }

}

