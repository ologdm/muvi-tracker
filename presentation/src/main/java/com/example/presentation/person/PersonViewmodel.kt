package com.example.presentation.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.IoResponse
import com.example.domain.model.Ids
import com.example.domain.model.Person
import com.example.domain.model.PersonCredit
import com.example.domain.repo.PersonRepository
import com.example.presentation.utils.ListStateContainerTwo
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

    private val _personCastCredits =
        MutableStateFlow(ListStateContainerTwo<PersonCredit>(emptyList()))
    val personCastCredits = _personCastCredits.asStateFlow()


    fun loadPersonDetail(personIds: Ids) {
        viewModelScope.launch {

            when (val response = personRepo.getPersonDetail(personIds)) {
                is IoResponse.Success -> {
                    _personState.value = StateContainerTwo(response.dataValue)
                }

                is IoResponse.Error -> {
                    _personState.value = StateContainerTwo(isError = true)
                }
            }
        }

//        loadPersonCastCredits()
    }


    // TODO: decidere tra ListStateContainerTwo/StateContainerTwo
    // ListStateContainerTwo - emptyList || List<PersonCredit>; never null
    fun loadPersonCastCredits(personIds: Ids) {
        viewModelScope.launch {

            val response = personRepo.getPersonCredits(personIds)
            if (response.isEmpty()) {
                _personCastCredits.value =
                    ListStateContainerTwo(data = emptyList(), isError = true)
            } else {
                _personCastCredits.value =
                ListStateContainerTwo(data = response)
            }

        }

    }
    // TEST: nicholas hoult, trakt 4718, tmdb 3292

}