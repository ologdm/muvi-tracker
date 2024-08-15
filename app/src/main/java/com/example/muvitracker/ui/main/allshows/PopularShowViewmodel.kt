package com.example.muvitracker.ui.main.allshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.shows.PopularPagingSourceS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularShowViewmodel @Inject constructor(
    private val traktApi: TraktApi
) : ViewModel() {

    val statePaging = Pager(
        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = { PopularPagingSourceS(traktApi) }
    ).flow
        .cachedIn(viewModelScope)
}