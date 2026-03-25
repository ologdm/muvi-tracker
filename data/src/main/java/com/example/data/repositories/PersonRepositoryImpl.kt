package com.example.data.repositories

import com.example.data.api.TmdbApi
import com.example.data.api.TraktApi
import com.example.data.dto.person.detail.mergePersonDtoToDomain
import com.example.domain.IoResponse
import com.example.domain.model.Ids
import com.example.domain.model.Person
import com.example.domain.repo.PersonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class PersonRepositoryImpl @Inject constructor(
    val traktApi: TraktApi,
    val tmdbApi: TmdbApi
) : PersonRepository {


    override suspend fun getPersonDetail(personIds: Ids): IoResponse<Person> {
        return try {
            // NOTE: supervisorScope not needed — Trakt is mandatory, TMDB is just a best-effort enrichment.
            coroutineScope {
                val traktDeferred = async {
                    traktApi.getPersonDetail(personIds.trakt) // da sempre risultato
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

                // tmdbDto can be null
                IoResponse.Success(dataValue = mergePersonDtoToDomain(traktDto, tmdbDto))
            }
        } catch (e: Exception) {
            IoResponse.Error(e)
        }
    }

}