package com.example.muvitracker.ui.main.detailmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.ui.main.detailshow.DetailShowRepository
import com.example.muvitracker.utils.IoResponse
import com.example.muvitracker.utils.StateContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailShowViewmodel @Inject constructor(
    val repo: DetailShowRepository
) : ViewModel() {


    val state = MutableLiveData<StateContainer<DetailShowDto>>()



    fun loadDetail(showId: Int) {
        viewModelScope.launch {
            val response = repo.getDetailData(showId)

            when (response) {
                is IoResponse.Success -> {
                    state.value = StateContainer(data = response.dataValue)
                }

                is IoResponse.Error -> {
                    state.value = StateContainer(isNetworkError = true)
                }
            }


        }
    }


}