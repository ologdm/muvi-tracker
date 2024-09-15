package com.example.muvitracker.ui.main.allmovies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.movies.MoviesPagingSource
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MoviesViewmodel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val traktApi: TraktApi,
    private val moviesRepository: MoviesRepo
) : ViewModel() {

    private var feedCategory = ""

    // paging observable
    val statePaging = Pager(
        config = PagingConfig(pageSize = 15, enablePlaceholders = false),
        pagingSourceFactory = {
            MoviesPagingSource(
                applicationContext,
                feedCategory,
                traktApi
            )
        }
    ).flow
        .cachedIn(viewModelScope) // scope a cui fa riferimento il paging attuale


    // boxo observable
    private val _boxoState = MutableLiveData<StateContainer<List<Movie>>>()
    val boxoState: LiveData<StateContainer<List<Movie>>> get() = _boxoState


    // paging categories
    fun getMoviesFromSelectFeedCategory(selectedCategory: String) {
        feedCategory = selectedCategory
    }


    // no paging, with caching
    fun loadBoxoMovies() {
        viewModelScope.launch {
            var maintainedData: List<Movie>? = null

            moviesRepository.getBoxoStoreStream()
                .catch {
                    it.printStackTrace()
                }
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            maintainedData = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                StateContainer(data = maintainedData, isNetworkError = true)
                            } else {
                                StateContainer(data = maintainedData, isOtherError = true)
                            }
                        }
                    }
                }.collectLatest {
                    _boxoState.value = it
                }
        }
    }

}






