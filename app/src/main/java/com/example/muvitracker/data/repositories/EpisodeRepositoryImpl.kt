package com.example.muvitracker.data.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.copyDtoData
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.episode.EpisodeExtenDto
import com.example.muvitracker.data.dto.episode.toEntity
import com.example.muvitracker.data.utils.ShowRequestKeys
import com.example.muvitracker.data.utils.mapToIoResponse
import com.example.muvitracker.data.utils.storeFactory
import com.example.muvitracker.domain.model.EpisodeExtended
import com.example.muvitracker.domain.repo.EpisodeRepository
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepositoryImpl @Inject constructor(
    private val traktApi: TraktApi,
    database: MyDatabase,
    private val prefsShowRepository: PrefsShowRepository
) : EpisodeRepository {
    private val episodeDao = database.episodesDao()

    val seasonEpisodesStore =
        storeFactory<ShowRequestKeys, List<EpisodeExtenDto>, List<EpisodeExtended>>(
            fetcher = { request ->
                traktApi.getSeasonWithEpisodes(request.showId, request.seasonNr)
            },
            reader = { request ->
                episodeDao.readAllOfSeason(request.showId, request.seasonNr)
                    .map { episodes ->
                        if (episodes.isEmpty()) null
                        else episodes.map { it.toDomain() }
                    }
            },
            writer = { request, episodeDtos ->
                saveDtosToDatabase(request.showId, episodeDtos)
            }
        )


    private suspend fun saveDtosToDatabase(showId: Int, dtoList: List<EpisodeExtenDto>) {
        // if (non esiste) insert new, else  partial update
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


    override
    fun getSeasonAllEpisodesFlow(
        showId: Int,
        seasonNr: Int
    ): Flow<IoResponse<List<EpisodeExtended>>> {
        return seasonEpisodesStore
            .stream(
                StoreRequest.cached(
                    ShowRequestKeys(showId = showId, seasonNr = seasonNr),
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
    ): Flow<EpisodeExtended?> {
        // read from db - single episode always on db
        return episodeDao.readSingle(showId, seasonNr, episodeNr).map {entity->
            entity?.toDomain()
        }
    }


    override
    suspend fun toggleSingleWatchedEpisode(
        showId: Int,
        seasonNr: Int,
        episodeNr: Int
    ) {
        episodeDao.toggleWatchedSingle(showId, seasonNr, episodeNr)
        prefsShowRepository.checkAndAddIfWatchedToPrefs(showId) // add show to prefs if episode watched
    }

}
