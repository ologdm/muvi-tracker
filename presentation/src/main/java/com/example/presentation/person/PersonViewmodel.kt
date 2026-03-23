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
    // moved to repo - new
//    val traktApi: TraktApi,
//    val tmdbApi: TmdbApi
    private val personRepo: PersonRepository //  NEW OK
) : ViewModel() {

    // NEW OK
    private val _personState = MutableStateFlow(StateContainerTwo<Person>(null))
    val personState = _personState.asStateFlow()


    // NEW OK
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



//            val traktDeferred = async {
//                try {
//                    traktApi.getPersonDetail(personIds.trakt) // -> da sempre risultato, parte da un PersonBase
//                } catch (ex: CancellationException) {
//                    throw ex
//                }
//            }
//
//            val tmdbDeferred = async {
//                try {
//                    tmdbApi.getPersonDto(personIds.tmdb)
//                } catch (ex: CancellationException) {
//                    throw ex
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    null
//                }
//            }
//
//            val traktDto = traktDeferred.await()
//            val tmdbDto = tmdbDeferred.await()
//
//            personState.value = mergePersonDtoToDomain(traktDto, tmdbDto)
        }
    }


}