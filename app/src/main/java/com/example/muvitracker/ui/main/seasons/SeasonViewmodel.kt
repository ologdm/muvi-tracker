package com.example.muvitracker.ui.main.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

// TODO - readFromDb

@HiltViewModel
class SeasonViewmodel @Inject constructor(
    val traktApi: TraktApi
) : ViewModel() {

    val seasonInfoState = MutableLiveData<StateContainer<SeasonExtenDto>>()
    val seasonEpisodesState = MutableLiveData<StateContainer<List<EpisodeExtenDto>>>()


    // OK
    fun loadSeasonInfo(showId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            try {
                val result = traktApi.getSeasonInfo(showId, seasonNumber)
                seasonInfoState.value = StateContainer(data = result)
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }


    // OK
    fun loadSeasonEpisodes(showId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            try {
                val result = traktApi.getSeasonWithEpisodes(showId, seasonNumber)
                seasonEpisodesState.value = StateContainer(data = result)
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }







}