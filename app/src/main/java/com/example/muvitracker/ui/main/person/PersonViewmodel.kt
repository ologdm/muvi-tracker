package com.example.muvitracker.ui.main.person

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TmdbApi
import com.example.data.TraktApi
import com.example.data.dto.person.detail.mergePersonDtoToDomain
import com.example.domain.model.Ids
import com.example.domain.model.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class PersonViewmodel @Inject constructor(
    val traktApi: TraktApi,
    val tmdbApi: TmdbApi
) : ViewModel() {

    val personState = MutableLiveData<Person>()


    fun getPersonDetail(personIds: Ids) {
        viewModelScope.launch {
            val traktDeferred = async {
                try {
                    traktApi.getPersonDetail(personIds.trakt) // -> da sempre risultato, parte da un PersonBase
                } catch (ex: CancellationException) {
                    throw ex
                }
            }

            val tmdbDeferred = async {
                try {
                    tmdbApi.getPersonDto(personIds.tmdb)
                } catch (ex: CancellationException) {
                    throw ex
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            val traktDto = traktDeferred.await()
            val tmdbDto = tmdbDeferred.await()

            personState.value = mergePersonDtoToDomain(traktDto, tmdbDto)
        }
    }


}