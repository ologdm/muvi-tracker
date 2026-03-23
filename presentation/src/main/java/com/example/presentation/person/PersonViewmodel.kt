package com.example.presentation.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.IoResponse
import com.example.domain.model.Ids
import com.example.domain.model.Person
import com.example.domain.repo.PersonRepository
import com.example.presentation.utils.StateContainerTwo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonViewmodel @Inject constructor(
    private val personRepo: PersonRepository
) : ViewModel() {

    private val _personState = MutableStateFlow(StateContainerTwo<Person>(null))
    val personState = _personState.asStateFlow()


    fun getPersonDetail(personIds: Ids) {
        viewModelScope.launch {
            // TODO: moved to repo OK
            viewModelScope.launch {
                val response = personRepo.getPersonDetail(personIds)
                when (response) {
                    is IoResponse.Success -> {
                        _personState.value = StateContainerTwo(response.dataValue)
                    }

                    is IoResponse.Error -> {
                        _personState.value = StateContainerTwo(isError = true)
                    }
                }
            }
        }
    }


}