package com.example.muvitracker.ui.main.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.dao.EpisodeDao
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.dto.episode.EpisodeExtenDto
import com.example.muvitracker.data.imagetmdb.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EpisodeViewmodel @Inject constructor(
    val database: MyDatabase,
    val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val episodeDao = database.episodesDao()


    val state = MutableLiveData<EpisodeEntity>()


    // se elemento nella RV, elemento già nel database
    fun loadEpisode(showTraktId: Int, seasonNr: Int, episodeNr: Int) {
        viewModelScope.launch {
            state.value = episodeDao.readSingleEpisode(showTraktId, seasonNr, episodeNr)
        }
    }


    // TMDB IMAGES
    val backdropImageUrl = MutableLiveData<String>()

    fun getTmdbImageLinksFlow(showTmdbId: Int, seasonNr: Int, episodeNr: Int) {
        viewModelScope.launch {
            val result = tmdbRepository
                .getEpisodeImageFlow(showTmdbId, seasonNr, episodeNr)
                .firstOrNull()
            val backdropUrl = result?.get(TmdbRepository.BACKDROP_KEY) ?: ""
            backdropImageUrl.value = backdropUrl
        }
    }


}