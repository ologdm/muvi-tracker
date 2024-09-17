package com.example.muvitracker.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.database.entities.toDomain
import com.example.muvitracker.data.dto.show.DetailShowDto
import com.example.muvitracker.data.dto.season.SeasonExtenDto
import com.example.muvitracker.data.dto.movies.toEntity
import com.example.muvitracker.data.dto.season.toEntity
import com.example.muvitracker.data.dto.show.toEntity
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.utils.IoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

// solo per detail fragment
// 1. detail base
// 2. season


@Singleton
class DetailShowRepository @Inject constructor(
    private val traktApi: TraktApi,
    private val database: MyDatabase
) {

    private val detailShowDao = database.detailShowDao()
    private val prefsShowDao = database.prefsShowDao()

    private val seasonDao = database.seasonsDao()


    // DETAIL 00
    // store
    private val detailStore: Store<Int, DetailShowEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { showId ->
            try {
                FetcherResult.Data(traktApi.getShowDetail(showId))

            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, DetailShowDto, DetailShowEntity>(
            reader = { showId ->
                detailShowDao.readSingleFlow(showId)
            },
            writer = { _, dto ->
                try {
                    detailShowDao.insertSingle(dto.toEntity())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        )
    ).build()


    fun getSingleDetailShowFlow(id: Int): Flow<IoResponse<DetailShow>> {
        // flow1
        val detailFlow = detailStore.stream(StoreRequest.cached(key = id, refresh = true))
            .filterNot { response ->
                response is StoreResponse.Loading || response is StoreResponse.NoNewData
            }
        // flow2
        val prefsListFLow = prefsShowDao.readAll()

        // combine flows T1, T2 -> R
        return detailFlow
            .combine(prefsListFLow) { storeResponse, prefList ->
                when (storeResponse) {
                    is StoreResponse.Data -> {
                        val prefsEntity = prefList.find { entity ->
                            entity?.traktId == storeResponse.value.ids.trakt
                        }
                        val detailMovie = storeResponse.value.toDomain(prefsEntity)
                        IoResponse.Success(detailMovie)
                    }

                    is StoreResponse.Error.Exception ->
                        IoResponse.Error(storeResponse.error)

                    is StoreResponse.Error.Message ->
                        IoResponse.Error(RuntimeException(storeResponse.message))

                    is StoreResponse.Loading,
                    is StoreResponse.NoNewData -> error("should be filtered upstream")
                }
            }
    }


    fun getDetailListFlow(): Flow<List<DetailShowEntity?>> {
        return detailShowDao.readAllFlow()
    }




    // SEASONS ###########################################################

    // 1. store 00
    val seasonStore: Store<Int, List<SeasonEntity>> = StoreBuilder.from(
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
        sourceOfTruth = SourceOfTruth.of<Int, List<SeasonExtenDto>, List<SeasonEntity>>(
            reader = { showId ->
                seasonDao.readAllSeasonsOfShow(showId) // flow
            },
            writer = { showId, dtos ->
                saveSeasonDtosToDatabase(showId, dtos)
            }
        )
    ).build()


    // 2. logica write season + aggiornamento detailShow  00
    suspend fun saveSeasonDtosToDatabase(showId: Int, dtos: List<SeasonExtenDto>) {
        // if (non esiste) insertNuovo, else updateParziale
        for (dto in dtos) {
            val dtoIndex = dto.ids.trakt
            val entity = seasonDao.readSingleSeasonById(dtoIndex).firstOrNull()
            if (entity == null) {
                seasonDao.insertSingle(dto.toEntity(showId))
            } else {
                val updatedEntity = entity.copy(
                    // TODO semplificare ala fine
                    seasonTraktId = dto.ids.trakt,
                    seasonNumber = dto.number,
                    ids = dto.ids,
                    rating = dto.rating,
                    episodeCount = dto.episodeCount,
                    airedEpisodes = dto.airedEpisodes,
                    title = dto.title,
                    overview = dto.overview ?: "",
                    releaseYear = dto.getYear(),
                    network = dto.network,
                    showId = showId
                )
            }
        }
    }


    // 3. stream 00
    fun getShowSeasonsFlow(showId: Int): Flow<IoResponse<List<SeasonEntity>>> {
        return seasonStore.stream(StoreRequest.cached(showId, refresh = true))
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





}
