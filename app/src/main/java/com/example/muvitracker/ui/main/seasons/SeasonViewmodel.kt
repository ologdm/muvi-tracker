package com.example.muvitracker.ui.main.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.EpisodeRepository
import com.example.muvitracker.data.SeasonRepository
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.imagetmdb.TmdbRepository
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SeasonViewmodel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    val seasonInfoState = MutableLiveData<StateContainer<SeasonExtended>>()
    val seasonEpisodesState = MutableLiveData<StateContainer<List<EpisodeEntity>>>() // test


    fun loadSeasonInfo(showId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            seasonRepository.getSingleSeasonFlow(showId, seasonNumber)
                .catch {
                    it.printStackTrace()
                }
                .collectLatest {seasonExtendedFromDb ->
                    seasonInfoState.value = StateContainer(seasonExtendedFromDb)
                }
        }
    }



fun loadSeasonEpisodes(showId: Int, seasonNumber: Int) {
    // caching come in details
    viewModelScope.launch {
        episodeRepository.getSeasonEpisodesFlow(showId, seasonNumber)
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
            }.collectLatest {
                seasonEpisodesState.value = it
            }
    }
}


fun toggleSeasonAllWatchedEpisodes(showId: Int, seasonNr: Int) {
    viewModelScope.launch {
        seasonRepository.checkAndToggleWatchedAllSeasonEpisodes(showId, seasonNr)
    }
}


fun toggleWatchedEpisode(
    showId: Int,
    seasonNr: Int,
    episodeNr: Int
) {
    viewModelScope.launch {
        episodeRepository.toggleSingleWatchedEpisode(showId, seasonNr, episodeNr)
    }
}


// TMDB IMAGES
val posterImageUrl = MutableLiveData<String>()

fun getTmdbImageLinksFlow(showTmdbId: Int, seasonNr: Int) {
    viewModelScope.launch {
        // todo gestione null !!!!!!!!!
        val result = tmdbRepository
            .getSeasonImageFlow(showTmdbId, seasonNr)
            .firstOrNull()
        val posterUrl = result?.get(TmdbRepository.POSTER_KEY) ?: ""
        posterImageUrl.value = posterUrl
    }
}


}
