package com.example.muvitracker.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.dto.toEntity
import com.example.muvitracker.data.requests.ShowRequestKeys
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// contiene lista episodi totale

@Singleton
class XSeasonRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase,
) {

    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()


    // EPISODES #################################################################
    // 1. store = solo lettura
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
                episodeDao.readAllEpisodesOfSeason(request.showId, request.seasonNr)

            },
            writer = { request, dtos ->
                // ciclo for che paragona id di ogni elemento prima di inserirlo
                saveDtosToDatabase(request.showId, dtos) // OK
            }
        )
    ).build()


    // 2. 00
    private suspend fun saveDtosToDatabase(showId: Int, dtos: List<EpisodeExtenDto>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (dto in dtos) { // check per ogni elemento dto
            val dtoIndex = dto.ids.trakt
            val entity = episodeDao.readSingleEpisodeById(dtoIndex).firstOrNull()
            if (entity == null) {
                episodeDao.insertSingle(dto.toEntity(showId))
            } else {
                val updatedEntity = entity.copy(
                    // TODO semplificare
                    episodeTraktId = dto.ids.trakt,
                    seasonNumber = dto.season,
                    episodeNumber = dto.number,
                    title = dto.title,
                    ids = dto.ids,
                    numberAbs = dto.numberAbs,
                    overview = dto.overview,
                    rating = dto.rating,
                    firstAiredFormatted = dto.getDateFromFirsAired(),
                    availableTranslations = dto.availableTranslations,
                    runtime = dto.runtime,
                    episodeType = dto.episodeType,
                    showId = showId,
                    //  watched rimane stato db
                )
                episodeDao.updateDataSingleEpisode(updatedEntity)
            }
        }
    }


    // 3. stream 00
    fun getSeasonEpisodesFlow(showId: Int, seasonNr: Int): Flow<IoResponse<List<EpisodeEntity>>> {
        return episodeStore.stream(
            StoreRequest.cached(
                ShowRequestKeys(showId = showId, seasonNr = seasonNr),
                refresh = true
            ) // TODO togliere refresh=true?
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


    // SEASON #################################################################
    // 1. solo lettura da db elemento singolo (elemento esiste sicuramente)
    // 2. no scrittura o aggiornamento
    fun getSingleSeasonFlow(showId: Int, seasonNr: Int): Flow<SeasonEntity> {
        return seasonDao.readSingleSeason(showId, seasonNr)
    }


    // WATCHED
    // 1. update episode 00
    suspend fun toggleWatchedEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ) {
        episodeDao.toggleWatchedSingleEpisode(showId, seasonNr, episodeNr)
    } // usare - seasonFragment, (DetailFragment no)


    // 2. update season
    suspend fun updateSeasonWatchedCountAndAll(showId: Int, seasonNr: Int) {
        val watchedEpisodes =
            episodeDao.checkWatchedEpisodesOfSeason(showId, seasonNr).firstOrNull()?.size
        val seasonTotalEpisodes =
            seasonDao.readSingleSeason(showId, seasonNr).firstOrNull()?.episodeCount

        if (watchedEpisodes == seasonTotalEpisodes) {
            // count=total  true
            seasonDao.updateWatchedCountOfSingleSeason(
                showId,
                seasonNr,
                watchedAll = true,
                watchedCount = watchedEpisodes ?: 0
            )
        } else {
            // count<total  false
            seasonDao.updateWatchedCountOfSingleSeason(
                showId,
                seasonNr,
                watchedAll = false,
                watchedCount = watchedEpisodes ?: 0
            )
        }
    }


    suspend fun toggleWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int,
    ) {
        // watched true, watchd false imperativo

        val seasonWatchedAll =
            seasonDao.readSingleSeason(showId, seasonNr).firstOrNull()?.watchedAll

        if (seasonWatchedAll == true) {
            episodeDao.toggleWatchedAllEpisodes(showId, seasonNr, false)
        } else {
            episodeDao.toggleWatchedAllEpisodes(showId, seasonNr, true)
        }


    }


    // TODO tmdb

}



