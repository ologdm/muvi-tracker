package com.example.muvitracker.ui.main.detailshow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.repositories.SeasonRepository
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.domain.model.base.Show
import com.example.muvitracker.domain.repo.DetailShowRepo
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import com.example.muvitracker.utils.ioMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel @Inject constructor(
    private val detailShowRepo: DetailShowRepo,
    private val seasonRepository: SeasonRepository,
    private val traktApi: TraktApi
) : ViewModel() {

    val detailState = MutableLiveData<StateContainer<DetailShow>>()
    val allSeasonsState = MutableLiveData<StateContainer<List<SeasonExtended>>>()
    val relatedShowsStatus = MutableLiveData<List<Show>>()
    val castState = MutableLiveData<CastAndCrew>()

    // SHOW
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


    fun loadAllSeasons(showId: Int) {
        viewModelScope.launch {
            seasonRepository.getAllSeasonsFlow(showId)
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


    // TOGGLE
    fun toggleLikedShow(showId: Int) {
        viewModelScope.launch {
            detailShowRepo.toggleLikedShow(showId)
        }
    }


    fun toggleWatchedAllShowEpisodes(showId: Int, onComplete: () -> Unit) {
        // add callback
        viewModelScope.launch {
            detailShowRepo.checkAndSetShowWatchedAllSeasons(showId)
            onComplete()
        }
    }

    fun toggleSingleSeasonWatchedAll(showId: Int, seasonNr: Int, onComplete: () -> Unit) {
        viewModelScope.launch() {
            // 1 start loading  - su adapter - start al click
            // 2 toggle allEpisodes + season + showOnPrefs
            seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(showId, seasonNr)
            // 3 finish loading - chiama la callback con true o false (se l'operazione ha avuto successo o no)
            onComplete()
        }
    }


    // RELATED SHOWS
    fun loadRelatedShows(showId: Int) {
        viewModelScope.launch {
            detailShowRepo.getRelatedShows(showId).ioMapper {
                relatedShowsStatus.value = it
            }
        }
    }


    // CAST ATTORI - same as the movie's
    fun loadCast(showId: Int) {
        viewModelScope.launch {
            try {
                castState.value = traktApi.getAllShowCast(showId).toDomain()
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }

}



