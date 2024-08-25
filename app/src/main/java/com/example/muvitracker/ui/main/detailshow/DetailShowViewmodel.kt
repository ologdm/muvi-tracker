package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.DetailShowRepository
import com.example.muvitracker.data.PrefsShowRepository
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.data.images.TmdbRepository
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel @Inject constructor(
    private val detailShowRepo: DetailShowRepository,
    private val prefsShowRepository: PrefsShowRepository,
    private val traktApi: TraktApi,
    val tmdbRepository: TmdbRepository
) : ViewModel() {

    val detailState = MutableLiveData<StateContainer<DetailShow>>()
    val allSeasonsState = MutableLiveData<StateContainer<List<SeasonEntity>>>()


    // flow -> livedata 00
    fun loadShowDetailFlow(showId: Int) {
        var cachedItem: DetailShow? = null
        viewModelScope.launch {
            detailShowRepo.getSingleDetailShowFlow(showId)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedItem = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                StateContainer(
                                    data = cachedItem,
                                    isNetworkError = true
                                )
                            } else {
                                StateContainer(
                                    data = cachedItem,
                                    isOtherError = true
                                )
                            }
                        }
                    }
                }.catch {
                    it.printStackTrace()
                }
                .collectLatest { container ->
                    detailState.value = container
                }
        }
    }


    // 00
    fun toggleLikedItem(id: Int) {
        viewModelScope.launch {
            prefsShowRepository.toggleLikedOnDB(id)
        }
    }


    fun loadAllSeasons(showId: Int) {
        viewModelScope.launch {
            detailShowRepo.getShowSeasonsFlow(showId)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            StateContainer(response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                StateContainer(isNetworkError = true)
                            } else {
                                StateContainer(isOtherError = true)
                            }
                        }
                    }
                }.catch {
                    it.printStackTrace()
                }.collectLatest { container ->
                    allSeasonsState.value = container
                }
        }

    }


    // TMDB TODO caching, ridurre dimensione no 4k, logica bestVotes
    val backdropImageUrl = MutableLiveData<String>()
    val posterImageUrl = MutableLiveData<String>()

    fun getTmdbImageLinks(showTmdbId: Int) { // TODO salvare link su entity detail
        viewModelScope.launch {
            val result = tmdbRepository.getShowImages(showTmdbId)
            val backdropUrl = result[TmdbRepository.BACKDROP_KEY] ?: ""
            val posterUrl = result[TmdbRepository.POSTER_KEY] ?: ""
            backdropImageUrl.value = backdropUrl
            posterImageUrl.value = posterUrl
        }
    }
}



