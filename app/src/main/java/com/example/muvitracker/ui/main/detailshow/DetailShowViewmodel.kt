package com.example.muvitracker.ui.main.detailshow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.muvitracker.utils.StateContainerTwo
import com.example.muvitracker.utils.ioMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel @Inject constructor(
    private val detailShowRepository: DetailShowRepository,
    private val seasonRepository: SeasonRepository,
) : ViewModel() {

    val detailState = MutableLiveData<StateContainerThree<Show>>()
    val allSeasonsState = MutableLiveData<StateContainerTwo<List<Season>>>()
    val relatedShowsState = MutableLiveData<StateContainerTwo<List<ShowBase>>>()
    val castState = MutableLiveData<StateContainerTwo<CastAndCrew>>()

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
                }
                .catch {
                    it.printStackTrace()
                }
                .collectLatest { container ->
                    // tipizzazione riconosciuta
                    detailState.value = container
                }
        }
    }


    // FLOW
    fun loadAllSeasons(showIds: Ids) {
        var cachedSeasonslist: List<Season>? = null

        viewModelScope.launch {
            seasonRepository.getAllSeasonsFlow(showIds)
                // mappo il flow da Ioresponse a StateContainer prima di collect
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedSeasonslist = response.dataValue
                            StateContainerTwo(response.dataValue, false)
                        }

                        is IoResponse.Error -> {
                            // tipizzazione da indicare, il problema e la lista
                            StateContainerTwo<List<Season>>(cachedSeasonslist, true)
                        }
                    }
                }
                .catch {
                    it.printStackTrace()
                }
                // raccolgo i dati trasformati
                .collectLatest { container ->
                    allSeasonsState.value = container
                }
        }

    }


    // SET, get automatico  TODO OK
    // 1
    fun toggleLikedShow(showId: Int) {
        viewModelScope.launch {
            detailShowRepository.toggleLikedShow(showId)
        }
    }

    // 2
    fun toggleWatchedAllShowEpisodes(showIds: Ids, onComplete: () -> Unit) {
        // add callback
        viewModelScope.launch {
            detailShowRepository.checkAndSetWatchedAllShowEpisodes(showIds)
            onComplete()
        }
    }

    // 3
    fun toggleWatchedAllSingleSeasonEp(showIds: Ids, seasonNr: Int, onComplete: () -> Unit) {
        viewModelScope.launch() {
            // 1 start loading  - su adapter - start al click
            // 2 toggle allEpisodes + season + showOnPrefs
            seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(showIds, seasonNr)
            // 3 finish loading - chiama la callback con true o false (se l'operazione ha avuto successo o no)
            onComplete()
        }
    }


    // RELATED SHOWS TODO 1.1.3 loading - TODO OK --------------------------------------------------
    //no flow
    fun loadRelatedShows(showId: Int) {
        viewModelScope.launch {
            val response = detailShowRepository.getRelatedShows(showId)
            when (response) {
                is IoResponse.Success -> {
                    relatedShowsState.value = StateContainerTwo(response.dataValue, false)
                }

                is IoResponse.Error -> {
                    relatedShowsState.value = StateContainerTwo(null, true)
                }
            }
        }
    }


    // CAST ATTORI - same as the movie's TODO OK
    fun loadCast(showId: Int) {
        viewModelScope.launch {
            viewModelScope.launch {
                val response = detailShowRepository.getShowCast(showId)
                when (response) {
                    is IoResponse.Success -> {
                        castState.value = StateContainerTwo(response.dataValue, false)
                    }

                    is IoResponse.Error -> {
                        castState.value = StateContainerTwo(null, true)
                    }
                }
            }
        }
    }

}



