package com.example.muvitracker.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.dto.EpisodeExtenDto
import com.example.muvitracker.data.requests.ShowRequestKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// contiene lista episodi totale

@Singleton
class XSeasonRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase
) {

    private val seasonDao = database.seasonsDao()
    private val episodeDao = database.episodesDao()


    // store = solo lettura ,
    // collego api episodes -> List<EpisodeExtenDto>
    // ottengo - List<Entity>
    val episodeStore: Store<ShowRequestKeys, List<EpisodeEntity>> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { request ->
            try {
                FetcherResult.Data(
                    traktApi.getSeasonWithEpisodes(
                        request.showId,
                        request.seasonNumber
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
                episodeDao.readAllEpisodesOfSeason(request.showId, request.seasonNumber)
            },
            writer = { request, dtos ->

                // ciclo for che paragona id di ogni elemento prima di inserirlo
                saveDtoToDatabase(request.showId, request.seasonNumber, dtos)
            }
        )
    ).build()



    private suspend fun saveDtoToDatabase(showId: Int, seasonNr: Int, dtos: List<EpisodeExtenDto>) {

    }
}






