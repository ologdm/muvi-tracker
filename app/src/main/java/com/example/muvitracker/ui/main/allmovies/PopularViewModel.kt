package com.example.muvitracker.ui.main.allmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.movies.PopularPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PopularViewModel @Inject constructor(
    private val traktApi: TraktApi
) : ViewModel() {

    val statePaging = Pager(
        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = { PopularPagingSource(traktApi) }
    ).flow
        .cachedIn(viewModelScope) // scope a cui fa riferimento il paging attuale




}






