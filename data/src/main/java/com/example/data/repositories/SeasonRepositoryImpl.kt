package com.example.data.repositories

import com.example.data.api.TmdbApi
import com.example.data.api.TraktApi
import com.example.data.database.MyDatabase
import com.example.data.database.entities.SeasonEntity
import com.example.data.dto.season.mergeSeasonsDtoToEntity
import com.example.data.utils.mapToIoResponse
import com.example.data.utils.storeFactory
import com.example.domain.model.Ids
import com.example.domain.model.Season
import com.example.domain.repo.EpisodeRepository
import com.example.domain.repo.PrefsShowRepository
import com.example.domain.repo.SeasonRepository
import com.example.domain.IoResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
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

    // TODO 1.1.3 store OK
    /**
     * Nel Fetcher, il tipo di ritorno di `DetailShowTmdbDto` è nullable.
     * Il Fetcher deve lanciare un'eccezione solo se l'intero processo di fetch fallisce.
     * Per evitare che un errore nel recupero da TMDB causi un `FetcherResult.Error.Exception(ex)` sullo Store,
     * utilizziamo un blocco try-catch e restituiamo `null` in caso di eccezione gestita.
     *
     * Nota: bisogna gestire correttamente anche le eccezioni di tipo `CancellationException`,
     */
    private val seasonStore = storeFactory<Ids, List<SeasonEntity>, List<Season>>(
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
            seasonDao.getAllSeasons(showIds.trakt) // return
            /** 1.1.3 - store reader -> deve dare sempre emptyList se non ha valori, not null
             * per corretto funzionamento della UI   */
//                .map { if (it.isEmpty()) null else it }  // eugi
        },
        writer = { showIds, entities ->
            saveAllSeasonsDtoToDatabase(showIds.trakt, entities)
        }
    )

    /*
    Store4 emette SEMPRE così, in questo ordine:
    1. Reader (cache) -> se il DB è vuoto: emette subito emptyList()
    2. Fetcher (rete) -> quando la rete risponde: emette la lista con i dati
    -> NON aspetta il fetch prima di emettere la prima emissione. È un comportamento corretto e voluto dal design di Store4.
     */

    // TODO OK 1.1.3
    // TICKET 1.0.0: Crash su click WatchedAllShow, in presenza di stagione specials, numero 0 - fixed OK
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
            } else { // 2. altrimenti aggiorna solo parte dto (dati da internet)
                // fix: copy non serve, uso direttamente update dato che già l'entity corretta
                seasonDao.updateSingle(seasonEntity)
            }
        }
    }


    // FIXME: fixed con idsData
    override
    fun getAllSeasonsFlow(showIds: Ids): Flow<IoResponse<List<Season>>> {
        val idsData = Ids(
            trakt = showIds.trakt,
            tmdb = showIds.tmdb,
            imdb = showIds.imdb,
            slug = showIds.slug
        )
        return seasonStore.stream(StoreReadRequest.cached(idsData, refresh = true))
            .mapToIoResponse()
    }

    override
    fun getSingleSeasonFlow(showId: Int, seasonNr: Int): Flow<Season> {
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


    // FIXME: idsData non serve
    // (click - single season)
    //  toggle single season -> from ShowFragment & SeasonFragment
    override
    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showIds: Ids,
        seasonNr: Int
    ) {
//        val idsData = Ids(
//            trakt = showIds.trakt,
//            tmdb = showIds.tmdb,
//            imdb = showIds.imdb,
//            slug = showIds.slug
//        )

        // 1. check and download all season episodes
        if (!areEpisodesAvailableForSeasonOnDB(showIds.trakt, seasonNr)) {
            // if (ep == 0), download episode
            episodeRepository.fetchSeasonEpisodes(showIds = showIds, seasonNr = seasonNr)
        }
        // 2. add show to prefs -> if isWatched  at least one episode
        prefsShowRepository.checkAndAddIfWatchedToPrefs(showIds.trakt)
        // 3 toggle all episodes from single season
        val seasonCountEpisodes =
            seasonDao.readSingle(showIds.trakt, seasonNr)?.episodeCount ?: 0
        val seasonWatchedAllEpisodes =
            episodeDao.countSeasonWatchedEpisodes(showIds.trakt, seasonNr)

        if (seasonWatchedAllEpisodes >= seasonCountEpisodes) {
            episodeDao.setSeasonWatchedAllEpisodes(showIds.trakt, seasonNr, false)
        } else {
            episodeDao.setSeasonWatchedAllEpisodes(showIds.trakt, seasonNr, true)
        }

        // todo egde case
        // - se puntate aumentano, devo watchedAll scompare, airedEpisodes 10->13, 3 episodi non scaricati
    }


    // FIXME:  idsData non serve
    // (click - show watchedAll)
    // from ShowDetail -> forceWatchedAll ()
    override
    suspend fun checkAndSetSingleSeasonWatchedAllEpisodes(
        showIds: Ids,
        seasonNr: Int,
        watchedAllState: Boolean
    ) {
        // 1. check and download all season episodes  ==
        if (!areEpisodesAvailableForSeasonOnDB(showIds.trakt, seasonNr)) {
            episodeRepository.fetchSeasonEpisodes(showIds = showIds, seasonNr = seasonNr)
        }
        // 2. add show to prefs -> if isWatched  at least one episode ==
        prefsShowRepository.checkAndAddIfWatchedToPrefs(showIds.trakt)
        // 3.
        episodeDao.setSeasonWatchedAllEpisodes(showIds.trakt, seasonNr, watchedAllState)
    }

}
