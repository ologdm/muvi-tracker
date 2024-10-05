package com.example.muvitracker.ui.main.person

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.dto.xperson.CastResponseDto
import com.example.muvitracker.data.dto.xperson.PersonExtendedDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonViewmodel @Inject constructor(
    val traktApi: TraktApi
) : ViewModel() {

    val personState = MutableLiveData<PersonExtendedDto>()


    fun getPerson(personId: Int) {
        viewModelScope.launch {
            try {
                personState.value = traktApi.getPersonDetail(personId)
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }


}