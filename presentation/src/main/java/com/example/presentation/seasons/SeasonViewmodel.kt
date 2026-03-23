package com.example.presentation.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TmdbApi
import com.example.data.TraktApi
import com.example.domain.model.Episode
import com.example.domain.model.Ids
import com.example.domain.model.Season
import com.example.domain.repo.EpisodeRepository
import com.example.domain.repo.SeasonRepository
import com.example.domain.IoResponse
import com.example.muvitracker.utils.ListStateContainerTwo
import com.example.muvitracker.utils.StateContainerThree
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonViewmodel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
    private val traktApi: TraktApi, // FIXME: possibile eliminare?
    private val tmdbApi: TmdbApi // FIXME: possibile eliminare?

) : ViewModel() {

    val seasonInfoState = MutableLiveData<StateContainerThree<Season>>()

    // return da episodeStore -> emptyList or data sempre
    val episodesState = MutableLiveData<ListStateContainerTwo<Episode>>()

    fun loadSeasonInfo(showId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            seasonRepository.getSingleSeasonFlow(showId, seasonNumber)
                .catch {
                    it.printStackTrace()
                }
                .collectLatest { seasonExtendedFromDb ->
                    seasonInfoState.value = StateContainerThree(seasonExtendedFromDb)
                }
        }
    }


    fun loadAllEpisodes(showIds: Ids, seasonNr: Int) {
        // anche se arriva l'errore con l'ultimo stato di store, lo prendo dalla cache dallo stato precedente
        var cache: List<Episode> = emptyList()

        viewModelScope.launch {
            var isFirstEmission = true // deve essere locale alla chiamata, per non interferire con viewpager
            // return da episodeStore -> emptyList or data sempre
            episodeRepository.getSeasonAllEpisodesFlow(showIds, seasonNr)
                .filter { response ->
                    // skippa solo la prima emissione empty
                    if (isFirstEmission && response is IoResponse.Success && response.dataValue.isEmpty() && cache.isEmpty()) {
                        isFirstEmission = false
                        false
                    } else {
                        true
                    }
                }
                .map { response ->
                    // for test
//                    delay(2000)
                    when (response) {

                        is IoResponse.Success -> {
                            cache = response.dataValue
                            ListStateContainerTwo(response.dataValue, false)
                        }

                        is IoResponse.Error -> {
                            ListStateContainerTwo<Episode>(cache, true)
                        }
                    }
                }
                .catch {
                    it.printStackTrace()
                }
                .distinctUntilChanged() // emette solo se cambia il container
                .collectLatest { container ->
                    episodesState.value = container
                }
        }
    }


    fun toggleSeasonAllWatchedEpisodes(
        showIds: Ids,
        seasonNr: Int
    ) {
        viewModelScope.launch {
            seasonRepository.checkAndSetSingleSeasonWatchedAllEpisodes(showIds, seasonNr)
        }
    }


    fun toggleWatchedEpisode(
        showIds: Ids,
        seasonNr: Int,
        episodeNr: Int
    ) {
        viewModelScope.launch {
            episodeRepository.toggleSingleWatchedEpisode(showIds, seasonNr, episodeNr)
        }
    }


}
