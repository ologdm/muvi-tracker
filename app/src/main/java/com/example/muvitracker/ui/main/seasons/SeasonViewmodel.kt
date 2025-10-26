package com.example.muvitracker.ui.main.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.domain.repo.EpisodeRepository
import com.example.muvitracker.domain.repo.SeasonRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainerThree
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SeasonViewmodel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    val seasonInfoState = MutableLiveData<StateContainerThree<SeasonExtended>>()
    val seasonEpisodesState = MutableLiveData<StateContainerThree<List<EpisodeExtended>>>() // test


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


    fun loadSeasonEpisodes(showIds: Ids, seasonNumber: Int) {
        // caching come in details
        viewModelScope.launch {
            episodeRepository.getSeasonAllEpisodesFlow(showIds, seasonNumber)
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
                }.collectLatest {
                    seasonEpisodesState.value = it
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
