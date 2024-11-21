package com.example.muvitracker.ui.main.allshows

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.shows.ShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ShowsViewmodel @Inject constructor(
    @ApplicationContext val applicationContext: Context,
    private val traktApi: TraktApi
) : ViewModel() {

    private var feedCategory = ""

    val statePaging = Pager(
        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = { ShowsPagingSource(applicationContext, feedCategory, traktApi) }
    ).flow
        .cachedIn(viewModelScope)


    fun getMoviesFromSelectFeedCategory(selectedCategory: String) {
        feedCategory = selectedCategory
    }

}

