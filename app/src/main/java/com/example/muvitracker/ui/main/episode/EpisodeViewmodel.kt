package com.example.muvitracker.ui.main.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.data.dto.EpisodeExtenDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EpisodeViewmodel @Inject constructor(): ViewModel() {

    val state = MutableLiveData<EpisodeExtenDto>()

    fun loadEpisode (){

    }

}