package com.example.muvitracker.ui.main.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.domain.repo.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EpisodeViewmodel @Inject constructor(
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    val state = MutableLiveData<EpisodeExtended?>()


    fun loadEpisode(showTraktId: Int, seasonNr: Int, episodeNr: Int) {
        viewModelScope.launch {
            episodeRepository.getSingleEpisode(showTraktId, seasonNr, episodeNr)
                .catch {
                    it.printStackTrace()
                }
                .collectLatest { episode ->
                    state.value = episode
                }
        }
    }


    fun toggleWatchedEpisode(showId: Int, seasonNr: Int, episodeNr: Int) {
        viewModelScope.launch {
            episodeRepository.toggleSingleWatchedEpisode(showId, seasonNr, episodeNr)
        }
    }


}