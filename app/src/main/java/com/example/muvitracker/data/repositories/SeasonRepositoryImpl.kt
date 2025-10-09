package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.copyDtoData
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.season.toEntity
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.SeasonExtended
import com.example.muvitracker.domain.repo.EpisodeRepository
import com.example.muvitracker.domain.repo.PrefsShowRepository
import com.example.muvitracker.domain.repo.SeasonRepository
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SeasonRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
    database: MyDatabase,
    private val episodeRepository: EpisodeRepository,
    private val prefsShowRepository: PrefsShowRepository
) : SeasonRepository {
    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()


    private val seasonStore = storeFactory<Int, List<SeasonExtenDto>, List<SeasonExtended>>(
        fetcher = { showId ->
            traktApi.getAllSeasons(showId)
        },
        reader = { showId ->
            seasonDao.getAllSeasons(showId)
                .map { if (it.isEmpty()) null else it }
        },
        writer = { showId, dtos ->
            saveAllSeasonsDtoToDatabase(showId, dtos)
        }
    )

    // TICKET: Crash su click WatchedAllShow, in presenza di stagione specials, numero 0
    private suspend fun saveAllSeasonsDtoToDatabase(showId: Int, dtos: List<SeasonExtenDto>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (seasonDto in dtos) {
            val seasonNumber = seasonDto.number
            if (seasonNumber == null || seasonNumber < 1) continue

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

    override
    fun getAllSeasonsFlow(showId: Int): Flow<IoResponse<List<SeasonExtended>>> {
        return seasonStore.stream(StoreRequest.cached(showId, refresh = true))
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
