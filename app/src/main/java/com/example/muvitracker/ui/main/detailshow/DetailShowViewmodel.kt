package com.example.muvitracker.ui.main.detailshow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.CastMember
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.domain.model.Season
import com.example.muvitracker.domain.model.base.ShowBase
import com.example.muvitracker.domain.repo.DetailShowRepository
import com.example.muvitracker.domain.repo.PrefsShowRepository
import com.example.muvitracker.domain.repo.SeasonRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.ListStateContainerTwo
import com.example.muvitracker.utils.StateContainerThree
import com.example.muvitracker.utils.StateContainerTwo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel @Inject constructor(
    private val detailShowRepository: DetailShowRepository,
    private val prefsShowRepository: PrefsShowRepository,
    private val seasonRepository: SeasonRepository,
) : ViewModel() {

    val showState = MutableLiveData<StateContainerThree<Show>>()

    // olds
//    val allSeasonsState = MutableLiveData<StateContainerTwo<List<Season>>>()
//    val relatedShowsState = MutableLiveData<StateContainerTwo<List<ShowBase>>>()
//    val castState = MutableLiveData<StateContainerTwo<CastAndCrew>>()
    // news
    val seasonsState = MutableLiveData<ListStateContainerTwo<Season>>()
    val relatedState = MutableLiveData<ListStateContainerTwo<ShowBase>>()
    val castState = MutableLiveData<ListStateContainerTwo<CastMember>>()


    private var showNotes = ""


    // SHOW - flow
    fun loadShowDetail(showIds: Ids) {
        var cachedItem: Show? = null

        viewModelScope.launch {
            detailShowRepository.getSingleDetailShowFlow(showIds)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedItem = response.dataValue
                            showNotes = response.dataValue.notes
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
                    showState.value = container
                }
        }
    }


    // FLOW
    fun loadAllSeasons(showIds: Ids) {
        var cache: List<Season> = emptyList()


        viewModelScope.launch {
            // deve essere locale alla chiamata, per non interferire con viewpager; // TODO non si azzera completamente se cambio velocemente pagine
            var isFirstEmission = true
            // for test
//            delay(2000)
            seasonRepository.getAllSeasonsFlow(showIds)
                // filer per corretta gestione stati ui, salta il primo se empty!!
                .filter { response ->
                    // skippa solo la prima emissione empty.
                    if (isFirstEmission && response is IoResponse.Success && response.dataValue.isEmpty() && cache.isEmpty()) {
                        isFirstEmission = false
                        false
                    } else {
                        true
                    }
                }
                // seasonStore -> return emptyList in caso stagioni mancanti -> imp!! per gestione stati ui
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cache = response.dataValue
                            ListStateContainerTwo(response.dataValue, false)
                        }

                        is IoResponse.Error -> {
                            // tipizzazione da indicare, il problema e la lista
                            ListStateContainerTwo<Season>(cache, true)
                        }
                    }
                }
                .catch {
                    it.printStackTrace()
                }
                .collectLatest { container ->
                    seasonsState.value = container
                }
        }

    }


    // RELATED SHOWS TODO 1.1.3 loading - TODO OK --------------------------------------------------
    //no flow
    fun loadRelatedShows(showId: Int) {
        viewModelScope.launch {
            // for test
//            delay(2000)
            val response = detailShowRepository.getRelatedShows(showId)
            when (response) {
                is IoResponse.Success -> {
                    relatedState.value = ListStateContainerTwo(response.dataValue, false)
                }

                is IoResponse.Error -> {
                    relatedState.value = ListStateContainerTwo(emptyList(), true)
                }
            }
        }
    }


    // CAST ATTORI - same as the movie's TODO OK
    fun loadCast(showId: Int) {
        viewModelScope.launch {
            // for test
//            delay(2000)
            val response = detailShowRepository.getShowCast(showId)
            when (response) {
                is IoResponse.Success -> {
                    if (response.dataValue.castMembers.isNotEmpty()) {
                        castState.value =
                            ListStateContainerTwo(response.dataValue.castMembers, false)
                    } else {
                        castState.value = ListStateContainerTwo(emptyList(), false)
                    }
                }

                is IoResponse.Error -> {
                    castState.value = ListStateContainerTwo(emptyList(), true)
                }
            }
        }

    }


    // SET, (reactive get) ----------------------------------------------------------------------
// 1
    fun toggleLikedShow(showId: Int) {
        viewModelScope.launch {
            prefsShowRepository.toggleLikedOnDB(showId)

        }
    }

    // 2 all show episodes
    fun toggleWatchedAllShow(showIds: Ids, onComplete: () -> Unit) {
        // add callback
        viewModelScope.launch {
            detailShowRepository.checkAndSetWatchedAllShowEpisodes(showIds)
            onComplete()
        }
    }


    // 3 all season episodes
    fun toggleWatchedAllSeason(showIds: Ids, seasonNr: Int, onComplete: () -> Unit) {
        viewModelScope.launch() {
            // 1 start loading  - su adapter - start al click
            // 2 toggle allEpisodes + season + showOnPrefs
            seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(showIds, seasonNr)
            // 3 finish loading - chiama la callback con true o false (se l'operazione ha avuto successo o no)
            onComplete()
        }
    }


    // 4
    fun setNotes(showId: Int, notes: String) {
        viewModelScope.launch {
            prefsShowRepository.setNotesOnDB(showId, notes)
            showNotes = notes
        }
    }

    fun getNotes(): String = showNotes

}



