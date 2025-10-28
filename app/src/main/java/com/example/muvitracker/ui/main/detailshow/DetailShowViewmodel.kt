package com.example.muvitracker.ui.main.detailshow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.person.toDomain
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.domain.model.Season
import com.example.muvitracker.domain.model.base.ShowBase
import com.example.muvitracker.domain.repo.DetailShowRepository
import com.example.muvitracker.domain.repo.SeasonRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainerThree
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
    private val detailShowRepository: DetailShowRepository,
    private val seasonRepository: SeasonRepository,
    private val traktApi: TraktApi
) : ViewModel() {

    val detailState = MutableLiveData<StateContainerThree<Show>>()
    val allSeasonsState = MutableLiveData<StateContainerThree<List<Season>>>()
    val relatedShowsStatus = MutableLiveData<List<ShowBase>>()
    val castState = MutableLiveData<CastAndCrew>()

    // SHOW - flow
    fun loadShowDetail(showId: Int) {
        var cachedItem: Show? = null
        viewModelScope.launch {
            detailShowRepository.getSingleDetailShowFlow(showId)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedItem = response.dataValue
                            StateContainerThree(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                StateContainerThree(
                                    data = cachedItem,
                                    isNetworkError = true
                                )
                            } else {
                                StateContainerThree(
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

    // flow
//    fun loadAllSeasons(showId: Int) {
    fun loadAllSeasons(showIds: Ids) {
        viewModelScope.launch {
//            seasonRepository.getAllSeasonsFlow(showId)
            seasonRepository.getAllSeasonsFlow(showIds)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            StateContainerThree(response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                StateContainerThree(isNetworkError = true)
                            } else {
                                StateContainerThree(isOtherError = true)
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
            detailShowRepository.toggleLikedShow(showId)
        }
    }


    fun toggleWatchedAllShowEpisodes(showIds: Ids, onComplete: () -> Unit) {
        // add callback
        viewModelScope.launch {
            detailShowRepository.checkAndSetWatchedAllShowEpisodes(showIds)
            onComplete()
        }
    }


    fun toggleWatchedAllSingleSeasonEp(showIds: Ids, seasonNr: Int, onComplete: () -> Unit) {
        viewModelScope.launch() {
            // 1 start loading  - su adapter - start al click
            // 2 toggle allEpisodes + season + showOnPrefs
            seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(showIds, seasonNr)
            // 3 finish loading - chiama la callback con true o false (se l'operazione ha avuto successo o no)
            onComplete()
        }
    }


    // RELATED SHOWS
    fun loadRelatedShows(showId: Int) {
        viewModelScope.launch {
            detailShowRepository.getRelatedShows(showId).ioMapper {
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



