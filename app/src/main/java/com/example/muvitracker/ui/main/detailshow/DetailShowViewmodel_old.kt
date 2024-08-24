package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.data.images.TmdbRepository
import com.example.muvitracker.ui.main.detailshow.DetailShowRepository_old
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel_old @Inject constructor(
    private val repo: DetailShowRepository_old,
    private val traktApi: TraktApi,
    val tmdbRepository: TmdbRepository
) : ViewModel() {

    val detailState = MutableLiveData<StateContainer<DetailShowDto>>()
    val allSeasonsState = MutableLiveData<StateContainer<List<SeasonExtenDto>>>()

    val backdropImageUrl = MutableLiveData<String>()
    val posterImageUrl = MutableLiveData<String>()


    // OK
    fun loadShowDetail(showId: Int) {
        viewModelScope.launch {
            val response = repo.getDetailData(showId)
            when (response) {
                is IoResponse.Success -> {
                    detailState.value = StateContainer(data = response.dataValue)
                }

                is IoResponse.Error -> {
                    detailState.value = StateContainer(isNetworkError = true)
                }
            }
        }
    }


    // OK
    fun loadAllSeasons(showId: Int) {
        viewModelScope.launch {
            try {
                val response = traktApi.getAllSeasons(showId)
                    .filter { it.number != 0 } // filter only the actual seasons
                allSeasonsState.value = StateContainer(data = response)
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }


    fun getTmdbImageLinks(showTmdbId: Int) { // TODO salvare link su entity detail
        viewModelScope.launch {
            val result = tmdbRepository.getShowImages(showTmdbId)
            val backdropUrl = result[TmdbRepository.BACKDROP_KEY] ?: ""
            val posterUrl = result[TmdbRepository.POSTER_KEY] ?: ""
            backdropImageUrl.value = backdropUrl
            posterImageUrl.value = posterUrl
        }

    }
}




//    // TODO
//    fun loadAllPeopleForAShow(showId: Int) {
//        // 1 cast(actors)
//        // 2 directing, writing dopo
//    }

