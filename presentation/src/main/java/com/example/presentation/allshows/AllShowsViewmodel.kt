package com.example.presentation.allshows

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.domain.ShowsType
import com.example.domain.repo.ExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


@HiltViewModel
class ShowsViewmodel @Inject constructor(
    // FIXME: sposta TraktApi su repo
    private val sharedPrefs: SharedPreferences,
    private val exploreRepo: ExploreRepository
) : ViewModel() {

    companion object {
        private const val SELECTED_FEED_KEY = "show_selected_feed_key"
    }

    private val _selectedFeed = MutableStateFlow(getLastFeed())
    val selectedFeed: StateFlow<ShowsType> = _selectedFeed

    val statePaging = selectedFeed
        .flatMapLatest { selectedFeed ->
            exploreRepo.getShowsByType(selectedFeed)
        }
        .cachedIn(viewModelScope)


    fun setFeed(selectedFeed: ShowsType) {
        _selectedFeed.value = selectedFeed
        sharedPrefs.edit().putString(SELECTED_FEED_KEY, selectedFeed.sharedPrefsValue).apply()
    }

    fun getLastFeed(): ShowsType {
        val stringValue = sharedPrefs.getString(SELECTED_FEED_KEY, null)
        return ShowsType.entries.find { it.sharedPrefsValue == stringValue } ?: ShowsType.Popular
    }

}



