package com.example.presentation.allmovies


import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.core.MovieType
import com.example.data.TraktApi
import com.example.data.repositories.paging.MoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


@HiltViewModel
class MoviesViewmodel @Inject constructor(
    // FIXME: spostare api su repo, presentation non ha accesso
    private val traktApi: TraktApi,
    private val sharedPrefs: SharedPreferences
) : ViewModel() {

    companion object {
        // key saved element in sharedPrefs
        private const val SELECTED_FEED_KEY = "movie_selected_feed_key"
    }

    // prende l'ultimo valore registrato su prefs, o Popular
    private val _selectedFeed = MutableStateFlow(getLastFeed()) // valore default
    val selectedFeed: StateFlow<MovieType> = _selectedFeed


    val statePaging = selectedFeed
        .flatMapLatest { selectedFeed ->
            Pager(
                config = PagingConfig(pageSize = 15, enablePlaceholders = false),
                pagingSourceFactory = { MoviesPagingSource(selectedFeed, traktApi) }
            ).flow
        }
        .cachedIn(viewModelScope)


    // SET/GET FEED - da sharedPrefs
    fun setFeed(selectedFeed: MovieType) {
        _selectedFeed.value = selectedFeed
        sharedPrefs.edit().putString(SELECTED_FEED_KEY, selectedFeed.sharedPrefsValue).apply()
    }

    fun getLastFeed(): MovieType {
        // from shared value -> MoviesType enum
        val stringValue = sharedPrefs.getString(SELECTED_FEED_KEY, null)
        return MovieType.entries.find { it.sharedPrefsValue == stringValue } ?: MovieType.Popular
    }

}








