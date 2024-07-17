package com.example.muvitracker.ui.main.allmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.base.Movie
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.utils.IoResponse2
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class PopularViewModel @Inject constructor(
    private val moviesRepository: MoviesRepo
) : ViewModel() {

    private val _state = MutableLiveData<StateContainer<List<Movie>>>() // write private
    val state: LiveData<StateContainer<List<Movie>>> get() = _state //read only

    init {
        loadMovies()
    }


    // coroutines ####################################################
    private fun loadMovies() {
        viewModelScope.launch {
            var maintainedData: List<Movie>? = null
            moviesRepository.getPopularStoreStream()
                .catch {
                    it.printStackTrace()
                }
                .map { response ->
                    when (response) {
                        is IoResponse2.Success -> {
                            maintainedData = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse2.Error -> {
                            if (response.t is IOException) {
                                StateContainer(data = maintainedData, isNetworkError = true)
                            } else {
                                StateContainer(data = maintainedData, isOtherError = true)
                            }
                        }
                    }
                }.collectLatest {
                    _state.value = it
                }
        }
    }
}




