package com.example.muvitracker.ui.main.allshows

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.muvitracker.R
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
    private val traktApi: TraktApi,
    private val sharedPrefs: SharedPreferences,
) : ViewModel() {

    companion object {
        private const val SELECTED_FEED_KEY = "show_selected_feed_key"
    }

    private val _selectedFeed = MutableStateFlow(getLastFeed())
    val selectedFeed: StateFlow<ShowsType> = _selectedFeed

    val statePaging = selectedFeed.flatMapLatest {
        Pager(
            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
            pagingSourceFactory = { ShowsPagingSource(applicationContext, it, traktApi) }
        ).flow
            .cachedIn(viewModelScope)
    }


    fun updateSelectedFeed(newSelectedFeed: ShowsType) {
        _selectedFeed.value = newSelectedFeed
        sharedPrefs.edit().putString(SELECTED_FEED_KEY, newSelectedFeed.sharedPrefsValue).apply()
    }

    fun getLastFeed(): ShowsType {
        val stringValue = sharedPrefs.getString(SELECTED_FEED_KEY, null)
        return ShowsType.entries.find { it.sharedPrefsValue == stringValue } ?: ShowsType.Popular
    }

}

enum class ShowsType {
    Popular,
    Watched,
    Favorites,
    ComingSoon;


    val stringRes: Int
        get() = when (this) {
            Popular -> R.string.popular
            Watched -> R.string.watched
            Favorites -> R.string.favorited
            ComingSoon -> R.string.anticipated
        }

    val sharedPrefsValue: String
        get() = when (this) {
            Popular -> "popular"
            Watched -> "watched"
            Favorites -> "favorited"
            ComingSoon -> "anticipated"
        }

}
