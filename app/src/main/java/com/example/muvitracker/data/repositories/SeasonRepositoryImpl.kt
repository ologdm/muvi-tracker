package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.dto.season.mergeSeasonsDtoToEntity
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.domain.repo.EpisodeRepository
import com.example.muvitracker.domain.repo.PrefsShowRepository
import com.example.muvitracker.domain.repo.SeasonRepository
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// OK 1.1.3
@Singleton
class SeasonRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
    private val tmdbApi: TmdbApi,
    database: MyDatabase,
    private val episodeRepository: EpisodeRepository,
    private val prefsShowRepository: PrefsShowRepository
) : SeasonRepository {
    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()

    // TODO 1.1.3 OK
//    private val seasonStore = storeFactory<Int, List<SeasonTraktDto>, List<SeasonExtended>>(
//        fetcher = { showId ->
//            traktApi.getAllSeasons(showId)
//        },
//        reader = { showId ->
//            seasonDao.getAllSeasons(showId)
//                .map { if (it.isEmpty()) null else it }
//        },
//        writer = { showId, dtos ->
//            saveAllSeasonsDtoToDatabase(showId, dtos)
//        }
//    )

    /**
     * Nel Fetcher, il tipo di ritorno di `DetailShowTmdbDto` è nullable.
     * Il Fetcher deve lanciare un'eccezione solo se l'intero processo di fetch fallisce.
     * Per evitare che un errore nel recupero da TMDB causi un `FetcherResult.Error.Exception(ex)` sullo Store,
     * utilizziamo un blocco try-catch e restituiamo `null` in caso di eccezione gestita.
     *
     * Nota: bisogna gestire correttamente anche le eccezioni di tipo `CancellationException`,
     */
    private val seasonStore = storeFactory<Ids, List<SeasonEntity>, List<SeasonExtended>>(
        fetcher = { showIds ->
            coroutineScope {
                val traktDeferred = async { traktApi.getAllSeasons(showIds.trakt) }
                val tmdbDeferred = async {
                    try {
                        tmdbApi.getShowDto(showIds.tmdb)
                    } catch (ex: CancellationException) {
                        throw  ex
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }

                val traktDtos = traktDeferred.await()
                val tmdbDtos = tmdbDeferred.await()?.seasons // TODO gestione null !!

                traktDtos.map { traktDto ->
                    val tmdbDto = tmdbDtos?.find { it.seasonNumber == traktDto.number }

                    mergeSeasonsDtoToEntity(showIds.trakt, traktDto, tmdbDto)
                }
            }
        },
        reader = { showIds ->
            seasonDao.getAllSeasons(showIds.trakt)
                .map { if (it.isEmpty()) null else it }
        },
        writer = { showIds, entities ->
            saveAllSeasonsDtoToDatabase(showIds.trakt, entities)
        }
    )


    // TODO OK 1.1.3
    // TICKET: Crash su click WatchedAllShow, in presenza di stagione specials, numero 0
//    private suspend fun saveAllSeasonsDtoToDatabase(showId: Int, dtos: List<SeasonTraktDto>) {
//        // if (non esiste) insertNuovo, else updateParziale
//        for (seasonDto in dtos) {
//            val seasonNumber = seasonDto.number
//            if (seasonNumber == null || seasonNumber < 1) continue
//
//            val dtoIndex = seasonDto.ids.trakt
//            val entity = seasonDao.readSingleById(dtoIndex)
//            if (entity == null) {
//                seasonDao.insertSingle(seasonDto.toEntity(showId))
//            } else {
//                val updatedEntity = entity.copyDtoData(seasonDto)
//                seasonDao.updateSingleByDto(updatedEntity)
//            }
//        }
//    }
    private suspend fun saveAllSeasonsDtoToDatabase(showId: Int, entities: List<SeasonEntity>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (seasonEntity in entities) {
            val seasonNumber = seasonEntity.seasonNumber
            // 1. salta season
            if (seasonNumber == null || seasonNumber < 1) continue

            val dtoIndex = seasonEntity.ids.trakt
            val entity = seasonDao.readSingleById(dtoIndex)
            // 2. se manca aggiungi
            if (entity == null) {
                seasonDao.insertSingle(seasonEntity)
                // 2. altrimenti aggiorna parte dto
            } else {
                // TODO OK 1.1.3
                // fix: copy non serve, uso direttamente update dato che già l'enetity corretta
//                val updatedEntity = entity.copyDtoData(seasonEntity)
                seasonDao.updateSingleByDto(seasonEntity)
            }
        }
    }


    //TODO OK 1.1.3
//    override
//    fun getAllSeasonsFlow(showId: Int): Flow<IoResponse<List<SeasonExtended>>> {
//        return seasonStore.stream(StoreRequest.cached(showId, refresh = true))
//            .mapToIoResponse()
//    }
    override
    fun getAllSeasonsFlow(showIds: Ids): Flow<IoResponse<List<SeasonExtended>>> {
        return seasonStore.stream(StoreRequest.cached(showIds, refresh = true))
            .mapToIoResponse()
    }

    override
    fun getSingleSeasonFlow(showId: Int, seasonNr: Int): Flow<SeasonExtended> {
        return seasonDao.getSingleSeason(showId, seasonNr)
            .map { season ->
                season
                    ?: throw IllegalStateException("Season not found for showId: $showId, seasonNr: $seasonNr")
            }
    }


    private suspend fun areEpisodesAvailableForSeasonOnDB(
        showId: Int,
        seasonNr: Int
    ): Boolean {
        return episodeDao.countEpisodesOfSeason(showId, seasonNr) > 0
    }


    // (click - single season)
    //  toggle single season -> from ShowFragment & SeasonFragment
    override
    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int
    ) {
        // 1. check and download all season episodes
        if (!areEpisodesAvailableForSeasonOnDB(showId, seasonNr)) {
            // if (ep == 0), download episodes
            episodeRepository.fetchSeasonEpisodes(showId = showId, seasonNr = seasonNr)
        }
        // 2. add show to prefs -> if isWatched  at least one episode
        prefsShowRepository.checkAndAddIfWatchedToPrefs(showId)
        // 3 toggle all episodes from single season
        val seasonCountEpisodes =
            seasonDao.readSingle(showId, seasonNr)?.episodeCount ?: 0
        val seasonWatchedAllEpisodes =
            episodeDao.countSeasonWatchedEpisodes(showId, seasonNr)

        if (seasonWatchedAllEpisodes >= seasonCountEpisodes) {
            episodeDao.setSeasonWatchedAllEpisodes(showId, seasonNr, false)
        } else {
            episodeDao.setSeasonWatchedAllEpisodes(showId, seasonNr, true)
        }

        // todo egde case
        // - se puntate aumentano, devo watchedAll scompare, airedEpisodes 10->13, 3 episodi non scaricati
    }


    // (click - show watchedAll)
    // from ShowDetail -> forceWatchedAll ()
    override
    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showId: Int,
        seasonNr: Int,
        watchedAllState: Boolean
    ) {
        // 1. check and download all season episodes  ==
        if (!areEpisodesAvailableForSeasonOnDB(showId, seasonNr)) {
            episodeRepository.fetchSeasonEpisodes(showId = showId, seasonNr = seasonNr)
        }
        // 2. add show to prefs -> if isWatched  at least one episode ==
        prefsShowRepository.checkAndAddIfWatchedToPrefs(showId)
        // 3.
        episodeDao.setSeasonWatchedAllEpisodes(showId, seasonNr, watchedAllState)
    }

}
