package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.copyDtoData
import com.example.muvitracker.data.dto.episode.EpisodeExtenDto
import com.example.muvitracker.data.dto.episode.toEntity
import com.example.muvitracker.data.utils.ShowRequestKeys
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class EpisodeRepository @Inject constructor(
    private val traktApi: TraktApi,
    database: MyDatabase,
    private val prefsShowRepository : PrefsShowRepository
) {

    private val episodeDao = database.episodesDao()


    val episodeStore: Store<ShowRequestKeys, List<EpisodeEntity>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { request ->
            try {
                FetcherResult.Data(
                    traktApi.getSeasonWithEpisodes(
                        request.showId,
                        request.seasonNr
                    )
                )
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<ShowRequestKeys, List<EpisodeExtenDto>, List<EpisodeEntity>>(
            reader = { request ->
                // flow
                episodeDao.readAllOfSeason(request.showId, request.seasonNr).map {
                    // map list -> to return list or null
                    if (it.isEmpty()) null
                    else it
                }
            },
            writer = { request, dtoList ->
                // ciclo for che paragona id di ogni elemento prima di inserirlo
                saveDtosToDatabase(request.showId, dtoList)
            }
        )
    ).build()


    private suspend fun saveDtosToDatabase(showId: Int, dtoList: List<EpisodeExtenDto>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (episodeDto in dtoList) { // check per ogni elemento dto
            val dtoIndex = episodeDto.ids.trakt
            val entity = episodeDao.readSingleById(dtoIndex)
            if (entity == null) {
                episodeDao.insertSingle(episodeDto.toEntity(showId))
            } else {
                // update only dto part (watchedState and showId don't change)
                val updatedEntity = entity.copyDtoData(episodeDto)
                episodeDao.updateDataOfSingle(updatedEntity)
            }
        }
    }



    fun getSeasonAllEpisodesFlow(showId: Int, seasonNr: Int): Flow<IoResponse<List<EpisodeEntity>>> {
        return episodeStore.stream(
            StoreRequest.cached(
                ShowRequestKeys(showId = showId, seasonNr = seasonNr),
                refresh = true
            )
        )
            .filterNot { storeResponse ->
                storeResponse is StoreResponse.Loading || storeResponse is StoreResponse.NoNewData
            }
            .map { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        IoResponse.Success(storeResponse.value)// no map toDomain
                    }

                    is StoreResponse.Error.Exception -> {
                        IoResponse.Error(storeResponse.error)
                    }

                    is StoreResponse.Error.Message -> {
                        IoResponse.Error(RuntimeException(storeResponse.message))
                    }

                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }


    // ########################################################################################
    // WATCHED TOGGLE
    suspend fun toggleSingleWatchedEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ) {
        episodeDao.toggleWatchedSingle(showId, seasonNr, episodeNr)
        prefsShowRepository.addWatchedToPrefs (showId) // add to prefs if watched
    }

}


//suspend fun toggleSeasonAllWatchedEpisodes(
//    //
//    showId: Int,
//    seasonNr: Int,
//) {
//        val seasonWatchedAll = seasonDao.readSingleSeason(showId, seasonNr)?.watchedAll
//        if (seasonWatchedAll == true) {
//            episodeDao.toggleWatchedAllEpisodes(showId, seasonNr, false)
//        } else {
//            episodeDao.toggleWatchedAllEpisodes(showId, seasonNr, true)
//        }