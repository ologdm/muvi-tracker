package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.copyDtoData
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.season.toEntity
import com.example.muvitracker.data.utils.ShowRequestKeys
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// contiene lista episodi totale

@Singleton
class SeasonRepository @Inject constructor(
    private val traktApi: TraktApi,
    database: MyDatabase,
    private val episodeRepository: EpisodeRepository,
    private val prefsShowRepository: PrefsShowRepository
) {
    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()


    // STORE
    private val seasonStore: Store<Int, List<SeasonExtended>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { showId ->
            try {
                FetcherResult.Data(traktApi.getAllSeasons(showId)
                    .filter { it.number != 0 })// exclude season 0, specials
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, List<SeasonExtenDto>, List<SeasonExtended>>(
            reader = { showId ->
                seasonDao.getAllSeasons(showId)
            },
            writer = { showId, dtos ->
                saveAllSeasonsDtoToDatabase(showId, dtos)
            }
        )
    ).build()


    private suspend fun saveAllSeasonsDtoToDatabase(showId: Int, dtos: List<SeasonExtenDto>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (seasonDto in dtos) {
            val dtoIndex = seasonDto.ids.trakt
            val entity = seasonDao.readSingleById(dtoIndex)
            if (entity == null) {
                seasonDao.insertSingle(seasonDto.toEntity(showId))
            } else {
                val updatedEntity = entity.copyDtoData(seasonDto)
                seasonDao.updateSingleByDto(updatedEntity)
            }
        }
    }


    fun getAllSeasonsFlow(showId: Int): Flow<IoResponse<List<SeasonExtended>>> {
        return seasonStore.stream(StoreRequest.cached(showId, refresh = true))
            .filterNot { storeResponse ->
                storeResponse is StoreResponse.Loading || storeResponse is StoreResponse.NoNewData
            }
            .map { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        IoResponse.Success(storeResponse.value)
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


    fun getSingleSeasonFlow(showId: Int, seasonNr: Int): Flow<SeasonExtended> {
        // !!deve essere flow perchè stato watchedAll puo sempre cambiare
        return seasonDao.getSingleSeason(showId, seasonNr)
    }


    private suspend fun areEpisodesAvailableForSeason(
        showId: Int,
        seasonNr: Int
    ): Boolean {
        return episodeDao.countEpisodesOfSeason(showId, seasonNr) > 0
    }



    // (click - single season)
    //  toggle single season -> from ShowFragment & SeasonFragment
    suspend fun checkAndSetSeasonWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int,
    ) {
        if (!areEpisodesAvailableForSeason(showId, seasonNr)) {
            // 1. download all season episodes
            episodeRepository.episodeStore
                .fresh(ShowRequestKeys(showId = showId, seasonNr = seasonNr))

            // 2. add show to prefs -> if isWatched  at least one episode
            prefsShowRepository.addWatchedToPrefs(showId)
        }

        // 3 toggle all episodes from single season
        val seasonCountEpisodes =
            seasonDao.readSingle(showId, seasonNr)?.episodeCount ?: 0

        val seasonWatchedAllEpisodes =
            episodeDao.countSeasonWatchedEpisodes(showId, seasonNr).first()

        if (seasonWatchedAllEpisodes == seasonCountEpisodes) {
            episodeDao.toggleSeasonWatchedAllEpisodes(showId, seasonNr, false)
        } else {
            episodeDao.toggleSeasonWatchedAllEpisodes(showId, seasonNr, true)
        }

        // todo egde case
        // - se puntate aumentano, devo watchedAll scompare, airedEpisodes 10->13, 3 episodi non scaricati
    }

    // (click - show watchedAll)
    // from ShowDetail -> forceWatchedAll ()
    suspend fun checkAndSetSeasonWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int,
        watchedAllState: Boolean
    ) {

        if (!areEpisodesAvailableForSeason(showId, seasonNr)) {
            // 1. download all season episodes ==
            episodeRepository.episodeStore.fresh(
                ShowRequestKeys(showId = showId, seasonNr = seasonNr)
            )

            // 2. add show to prefs -> if isWatched  at least one episode ==
            prefsShowRepository.addWatchedToPrefs(showId)
        }
        // 3.
        episodeDao.toggleSeasonWatchedAllEpisodes(showId, seasonNr, watchedAllState)
    }


}
