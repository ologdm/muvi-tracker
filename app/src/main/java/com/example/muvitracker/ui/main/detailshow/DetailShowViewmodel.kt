package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.DetailShowRepository
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.SeasonExtenDto
import com.example.muvitracker.data.images.TmdbRepository
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel @Inject constructor(
    private val detailShowRepo: DetailShowRepository,
    private val traktApi: TraktApi,
    val tmdbRepository: TmdbRepository
) : ViewModel() {

    val detailState = MutableLiveData<StateContainer<DetailShow>>()
    val allSeasonsState = MutableLiveData<StateContainer<List<SeasonExtenDto>>>() // TODO


    // flow -> livedata
    fun loadShowDetailFlow(showId: Int) {
        var cachedItem: DetailShow? = null
        viewModelScope.launch {
            detailShowRepo.getSingleDetailMovieFlow(showId)
                .map { response ->
                    when (response) {
                        is IoResponse.Success -> {
                            cachedItem = response.dataValue
                            StateContainer(data = response.dataValue)
                        }

                        is IoResponse.Error -> {
                            if (response.t is IOException) {
                                StateContainer(
                                    data = cachedItem,
                                    isNetworkError = true
                                )
                            } else {
                                StateContainer(
                                    data = cachedItem,
                                    isOtherError = true
                                )
                            }
                        }
                    }
                }.catch {
                    it.printStackTrace()
                }
                .collectLatest {container->
                    detailState.value = container
                }
        }
    }

    // 00
    fun toggleLikedItem(id: Int) {
        viewModelScope.launch {
            detailShowRepo.toggleLikedOnDb(id)
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


    // TODO ricopiare tmdb e appunti da l'altra
}



// old
//fun loadShowDetail(showId: Int) {
//    viewModelScope.launch {
//        val response = detailShowRepo.getDetailData(showId)
//        when (response) {
//            is IoResponse.Success -> {
//                detailState.value = StateContainer(data = response.dataValue)
//            }
//
//            is IoResponse.Error -> {
//                detailState.value = StateContainer(isNetworkError = true)
//            }
//        }
//    }
//}


