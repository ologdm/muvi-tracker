package com.example.data.repositories

import com.example.data.TmdbApi
import com.example.data.TraktApi
import com.example.data.database.MyDatabase
import com.example.data.database.entities.EpisodeEntity
import com.example.data.database.entities.copyOnlyDtoData
import com.example.data.database.entities.toDomain
import com.example.data.dto.episode.mergeEpisodeDtos
import com.example.data.utils.ShowRequestKeys
import com.example.data.utils.mapToIoResponse
import com.example.data.utils.storeFactory
import com.example.domain.model.Episode
import com.example.domain.model.Ids
import com.example.domain.repo.EpisodeRepository
import com.example.domain.repo.PrefsShowRepository
import com.example.domain.utils.IoResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
    private val tmdb: TmdbApi,
    database: MyDatabase,
    private val prefsShowRepository: PrefsShowRepository
) : EpisodeRepository {
    private val episodeDao = database.episodesDao()

    // TODO 1.1.3 store OK
    /**
     * Nel Fetcher, il tipo di ritorno di `Tmdb` è nullable.
     * Il Fetcher deve lanciare un'eccezione solo se l'intero processo di fetch fallisce.
     * Per evitare che un errore nel recupero da TMDB causi un `FetcherResult.Error.Exception(ex)` sullo Store,
     * utilizziamo un blocco try-catch e restituiamo `null` in caso di eccezione gestita.
     *
     * Nota: bisogna gestire correttamente anche le eccezioni di tipo `CancellationException`,
     */
    val episodesStore =
        storeFactory<ShowRequestKeys, List<EpisodeEntity>, List<Episode>>(
            // 1.1.3 fetcher OK
            fetcher = { request ->
                coroutineScope {
                    val traktDeferred = async {
                        traktApi.getSeasonWithEpisodes(request.showIds.trakt, request.seasonNr)
                    }
                    // chiamata aggiuntiva, non deve dar errore
                    val tmdbDeferred = async {
                        try {
                            tmdb.getSeasonDto(request.showIds.tmdb, request.seasonNr)
                        } catch (ex: CancellationException) {
                            throw ex
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    }

                    val traktDtos = traktDeferred.await()
                    val tmdbDtos =
                        tmdbDeferred.await()?.episodes // da dto season specifica, solo per episodi

                    val returnList = traktDtos.map { traktEpisodeDto ->
                        val tmdbDto = tmdbDtos?.find { it ->
                            traktEpisodeDto.number == it.episodeNumber
                        }
                        mergeEpisodeDtos(request.showIds.trakt, traktEpisodeDto, tmdbDto)
                    }
                    returnList // return emptylist
                }
            },
            // 1.1.3 reader OK
            reader = { request ->
                episodeDao.readAllOfSeason(request.showIds.trakt, request.seasonNr)
                    .map { episodes ->
                        /** 1.1.3 - store reader -> deve dare sempre emptyList se non ha valori, not null
                         * per corretto funzionamento della UI   */
//                        if (episodes.isEmpty()) null // consiglio eugi, uso empty list per far funzionare il flow
//                        else
                        episodes.map { it.toDomain() }
                    }
            },
            // 1.1.3  writer OK
            writer = { request, episodeEntities ->
                saveEntitiesOnDatabase(episodeEntities)
            }
        )


    // TODO 1.1.3 OK
    /** Insert new or partial update (without 'watched' state, used for backend logic)
     */
    private suspend fun saveEntitiesOnDatabase(entities: List<EpisodeEntity>) {
        for (entity in entities) { // check per ogni elemento dto
            val entityIndex = entity.ids.trakt
            val oldEntity = episodeDao.readSingleById(entityIndex)
            if (oldEntity == null) {
                episodeDao.insertSingle(entity)
            } else {
                episodeDao.updateSingle(entity.copyOnlyDtoData(oldEntity.watched))
            }
        }
    }


    override suspend fun fetchSeasonEpisodes(showIds: Ids, seasonNr: Int) {
        // FIXME: fixed con idsData
//        val idsData = Ids(
//            trakt = showIds.trakt,
//            tmdb = showIds.tmdb,
//            imdb = showIds.imdb,
//            slug = showIds.slug
//        )

//        episodesStore.fresh(ShowRequestKeys(idsData, seasonNr))
        episodesStore.fresh(ShowRequestKeys(showIds, seasonNr))
    }



    override
    fun getSeasonAllEpisodesFlow(
        showIds: Ids,
        seasonNr: Int
    ): Flow<IoResponse<List<Episode>>> {
        // FIXME: fixed con idsData
//        val idsData = Ids(
//            trakt = showIds.trakt,
//            tmdb = showIds.tmdb,
//            imdb = showIds.imdb,
//            slug = showIds.slug
//        )

        return episodesStore
            .stream(
                StoreReadRequest.cached(
//                    ShowRequestKeys(showIds = idsData, seasonNr = seasonNr),
                    ShowRequestKeys(showIds = showIds, seasonNr = seasonNr),
                    refresh = true
                )
            )
            .mapToIoResponse()
    }


    override
    fun getSingleEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ): Flow<Episode?> {
        // read from db - single episode always already on db
        return episodeDao.readSingle(showId, seasonNr, episodeNr).map { entity ->
            entity?.toDomain()
        }
    }


    //FIXME: IdsDomain
    override
    suspend fun toggleSingleWatchedEpisode(
        showIds: Ids,
        seasonNr: Int,
        episodeNr: Int
    ) {
        episodeDao.toggleWatchedSingle(showIds.trakt, seasonNr, episodeNr)
        prefsShowRepository.checkAndAddIfWatchedToPrefs(showIds.trakt) // add show to prefs if episode watched
    }

}
