package com.example.muvitracker.ui.main.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.detail.DetailRepositoryTest
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.utils.IoResponse2
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
//    private val detailRepository: DetailRepo,
    private val detailRepositoryTest: DetailRepositoryTest,
    private val prefsRepository: PrefsRepo
) : ViewModel() {

    val state = MutableLiveData<StateContainer<DetailMovie>>()



    // flow
    // response -> container
    fun getStateContainer(movieId: Int) {
        var cachedMovie: DetailMovie? = null

        viewModelScope.launch {
            detailRepositoryTest.getSingleDetailMovieFlow(movieId)
                .map { response ->
                    when (response) {
                        is IoResponse2.Success -> {
                            println("ZZZ_VM_S${response.dataValue}")
                            cachedMovie = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse2.Error -> {
                            if (response.t is IOException) {
                                println("ZZZ_VM_E1${response.t}")
                                StateContainer(
                                    data = cachedMovie,
                                    isNetworkError = true
                                )
                            } else {
                                println("ZZZ_VM_E2${response.t}")
                                StateContainer(
                                    data = cachedMovie,
                                    isOtherError = true
                                )
                            }
                        }
                    }
                }.collectLatest { container ->
                    state.value = container
                }
        }
    }


    fun toggleFavorite(id: Int) {
        prefsRepository.toggleFavoriteOnDB(id)
    }


    fun updateWatched(id: Int, watched: Boolean) {
        prefsRepository.updateWatchedOnDB(id, watched)
    }



}

//    fun getStateContainer(movieId: Int): LiveData<StateContainer<DetailMovie>> {
//        return detailRepository.getDetailMovie(movieId)
//            .map { repoRespopnse ->
//                when (repoRespopnse) {
//                    is IoResponse.Success -> {
//                        StateContainer(data = repoRespopnse.dataValue)
//                    }
//
//                    is IoResponse.NetworkError -> {
//                        StateContainer(isNetworkError = true)
//                    }
//
//                    is IoResponse.OtherError -> {
//                        StateContainer(isOtherError = true)
//                    }
//                }
//            }
//    }