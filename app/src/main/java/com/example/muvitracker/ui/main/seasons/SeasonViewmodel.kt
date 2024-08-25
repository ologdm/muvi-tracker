package com.example.muvitracker.ui.main.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.PrefsShowRepository
import com.example.muvitracker.data.XSeasonRepository
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// TODO - readFromDb

@HiltViewModel
class SeasonViewmodel @Inject constructor(
    private val seasonRepository: XSeasonRepository,
    private val prefsShowRepository: PrefsShowRepository
) : ViewModel() {

// old
//    val seasonEpisodesState = MutableLiveData<StateContainer<List<EpisodeExtenDto>>>()
//    val seasonInfoState = MutableLiveData<StateContainer<SeasonExtenDto>>() // test

    val seasonInfoState = MutableLiveData<StateContainer<SeasonEntity>>()
    val seasonEpisodesState = MutableLiveData<StateContainer<List<EpisodeEntity>>>() // test


    fun loadSeasonInfo(showId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            seasonRepository.getSingleSeasonFlow(showId, seasonNumber)
                .map { respose ->
                    StateContainer(data = respose)
                }.catch {
                    it.printStackTrace()
                }.collectLatest {
                    seasonInfoState.value = it
                }
        }
    }


    fun loadSeasonEpisodes(showId: Int, seasonNumber: Int) {
        // TODO caching come in details
        viewModelScope.launch {
            seasonRepository.getSeasonEpisodesFlow(showId, seasonNumber)
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


    fun toggleWatchedEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ) {
        viewModelScope.launch {
            seasonRepository.toggleWatchedEpisode(showId, seasonNr, episodeNr)
            seasonRepository.updateSeasonWatchedCountAndAll(showId, seasonNr)
//            prefsShowRepository.updateWatchedOnDB(showId) TODO
        }
    }


    fun toggleWatchedAllEpisodes(showId: Int, seasonNr: Int) {
        viewModelScope.launch {
            seasonRepository.toggleWatchedAllEpisodes(showId,seasonNr) // OK
            seasonRepository.updateSeasonWatchedCountAndAll(showId, seasonNr) // OK
//            prefsShowRepository.updateWatchedOnDB(showId) // TODO
        }
    }

}



// old
//fun loadSeasonEpisodes(showId: Int, seasonNumber: Int) {
//    viewModelScope.launch {
//        try {
//            val result = traktApi.getSeasonWithEpisodes(showId, seasonNumber)
//            seasonEpisodesState.value = StateContainer(data = result)
//        } catch (ex: CancellationException) {
//            throw ex
//        } catch (ex: Throwable) {
//            ex.printStackTrace()
//        }
//    }
//}


// old
//    fun loadSeasonInfo(showId: Int, seasonNumber: Int) {
//        viewModelScope.launch {
//            try {
//                val result = traktApi.getSeasonInfo(showId, seasonNumber)
//                seasonInfoState.value = StateContainer(data = result)
//            } catch (ex: CancellationException) {
//                throw ex
//            } catch (ex: Throwable) {
//                ex.printStackTrace()
//            }
//        }
//    }
