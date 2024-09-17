package com.example.muvitracker.data.imagetmdb

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.imagetmdb.database.entities.EpisodeImageEntity
import com.example.muvitracker.data.imagetmdb.database.entities.MovieShowImageEntity
import com.example.muvitracker.data.imagetmdb.database.entities.PersonImageEntity
import com.example.muvitracker.data.imagetmdb.database.entities.SeasonImageEntity
import com.example.muvitracker.data.imagetmdb.dto.EpisodeImageDto
import com.example.muvitracker.data.imagetmdb.dto.MovieShowImagesDto
import com.example.muvitracker.data.imagetmdb.dto.PersonImageDto
import com.example.muvitracker.data.imagetmdb.dto.SeasonImageDto
import com.example.muvitracker.data.imagetmdb.dto.filterNotUhdBackdrops
import com.example.muvitracker.data.imagetmdb.dto.filterNotUhdPosters
import com.example.muvitracker.data.imagetmdb.dto.toEntity
import com.example.muvitracker.data.requests.ShowRequestKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

// - backdrop ( ratio 1,78 - 2160/3840 - 1080/1920)
// - poster   ( ratio 0,67 - 3000/2000 - 1500/1000)
// - still    ( ratio 1,78 - 2160/3840 - 1080/1920) == backdrop
// - profile  ( ratio 0,67 - 3000/2000 - 1500/1000) == poster
// filter 4k - done

// movie ok, show ok, season ok, episode ok, person ok
// database completo, read, write single element


@Singleton
class TmdbRepository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val database: MyDatabase
) {

    companion object {
        const val BACKDROP_KEY = "backdrop_key"
        const val POSTER_KEY = "poster_key"

        const val TMDB_LINK = "https://image.tmdb.org/t/p/original"
    }


    private val movieShowImageDao = database.movieShowImageDao()
    private val seasonImageDao = database.seasonImageDao()
    private val episodeImageDao = database.episodeImageDao()
    private val personImageDao = database.personImageDao()


    // MOVIE  #############################################################################
    private val moviesImageStore: Store<Int, MovieShowImageEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { movieId ->
            try {
                FetcherResult.Data(tmdbApi.getMovieImages(movieId))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, MovieShowImagesDto, MovieShowImageEntity>(
            reader = { movieId ->
                movieShowImageDao.readSingle(movieId) // flow
            },
            writer = { _, dto ->
                movieShowImageDao.insertSingleElement(dto.toEntity())
            }
        )
    ).build()


    fun getMovieImageFlow(showId: Int): Flow<Map<String, String>> {
        return moviesImageStore.stream(StoreRequest.cached(key = showId, refresh = true))
            .filterNot {
                it is StoreResponse.Loading || it is StoreResponse.NoNewData
            }
            .map { response ->
                val entity = response.requireData()
                val bestBackdrop = entity.backdrops
                    .filterNotUhdBackdrops()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                val bestPoster = entity.posters
                    .filterNotUhdPosters()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                mapOf(
                    BACKDROP_KEY to "$TMDB_LINK$bestBackdrop",
                    POSTER_KEY to "$TMDB_LINK$bestPoster"
                )
            }
    }


    // SHOW  #############################################################################
    private val showsImageStore: Store<Int, MovieShowImageEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { showId ->
            // try catch
            try {
                FetcherResult.Data(tmdbApi.getShowImages(showId))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, MovieShowImagesDto, MovieShowImageEntity>(
            reader = { showId ->
                movieShowImageDao.readSingle(showId) // flow
            },
            writer = { _, dto ->
                movieShowImageDao.insertSingleElement(dto.toEntity())
            }
        )
    ).build()


    fun getShowImageFlow(showId: Int): Flow<Map<String, String>> {
        val response = showsImageStore.stream(StoreRequest.cached(key = showId, refresh = true))
            .filterNot {
                it is StoreResponse.Loading || it is StoreResponse.NoNewData
            }
            .map { response ->

                val entity = response.requireData()
                val bestBackdrop = entity.backdrops
                    .filterNotUhdBackdrops()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                val bestPoster = entity.posters
                    .filterNotUhdPosters()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                mapOf(
                    BACKDROP_KEY to "$TMDB_LINK$bestBackdrop",
                    POSTER_KEY to "$TMDB_LINK$bestPoster"
                )
            }

        return response
    }


    // SEASON  #############################################################################
    // call - showId, seasonNr
    // response - (Id, mediaItem)
    private val seasonImageStore: Store<ShowRequestKeys, SeasonImageEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { requestKeys ->
            try {
                FetcherResult.Data(
                    tmdbApi.getSeasonImages(
                        requestKeys.showId,
                        requestKeys.seasonNr
                    )
                )
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<ShowRequestKeys, SeasonImageDto, SeasonImageEntity>(
            reader = { requestKeys ->
                seasonImageDao.readSingle(requestKeys.showId, requestKeys.seasonNr) // flow
            },
            writer = { requestKeys, dto ->
                seasonImageDao.insertSingleElement(
                    dto.toEntity(
                        requestKeys.showId,
                        requestKeys.seasonNr
                    )
                )
            }
        )
    ).build()

    // posters
    fun getSeasonImageFlow(showId: Int, seasonNr: Int): Flow<Map<String, String>> {
        return seasonImageStore.stream(
            StoreRequest.cached(key = ShowRequestKeys(showId, seasonNr), refresh = true)
        )
            .filterNot {
                it is StoreResponse.Loading || it is StoreResponse.NoNewData
            }
            .map { response ->
                val bestPoster = response.requireData()
                    .posters
                    .filterNotUhdPosters()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                mapOf(
                    POSTER_KEY to "$TMDB_LINK$bestPoster"
                )
            }
    }

    // todo test
    suspend fun getSeasonTest(showTmdbId: Int, seasonNumber: Int): Map<String, String> {
        return try {
            val response = tmdbApi.getSeasonImages(showTmdbId, seasonNumber)

            val bestPoster = response.posters
                .maxByOrNull { it.voteCount }
                ?.filePath ?: ""
            mapOf(
                POSTER_KEY to "$TMDB_LINK$bestPoster"
            )
        } catch (ex: Throwable) {
            ex.printStackTrace()
            emptyMap()
        }
    }


    // EPISODE  #############################################################################
    // call - showId, seasonNr, episodeNr
    // response - (Id, mediaItem)
    // backdrops
    private val episodeImageStore: Store<ShowRequestKeys, EpisodeImageEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { requestKeys ->
            try {
                FetcherResult.Data(
                    tmdbApi.getEpisodeImages(
                        requestKeys.showId,
                        requestKeys.seasonNr,
                        requestKeys.episodeNr
                    )
                )
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<ShowRequestKeys, EpisodeImageDto, EpisodeImageEntity>(
            reader = { requestKeys ->
                episodeImageDao.readSingle(
                    showId = requestKeys.showId,
                    seasonNr = requestKeys.seasonNr,
                    episodeNr = requestKeys.episodeNr
                ) // flow
            },
            writer = { requestKeys, dto ->
                episodeImageDao.insertSingleElement(
                    dto.toEntity(
                        tmdbShowId = requestKeys.showId,
                        seasonNr = requestKeys.seasonNr,
                        episodeNr = requestKeys.episodeNr
                    )
                )
            }
        )
    ).build()


    fun getEpisodeImageFlow(showId: Int, seasonNr: Int, episodeNr: Int): Flow<Map<String, String>> {
        return episodeImageStore.stream(
            StoreRequest.cached(key = ShowRequestKeys(showId, seasonNr, episodeNr), refresh = true)
        )
            .filterNot {
                it is StoreResponse.Loading || it is StoreResponse.NoNewData
            }
            .map { response ->
                val bestBackdrop = response.requireData()
                    .stills
                    .filterNotUhdBackdrops()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                mapOf(
                    BACKDROP_KEY to "$TMDB_LINK$bestBackdrop"
                )
            }
    }


    // PERSON  #############################################################################
    // call - personId==tmdbId
    // response - (id==tmdb, mediaItem)
    // posters
    private val personImageStore: Store<Int, PersonImageEntity> = StoreBuilder.from(
        fetcher = Fetcher.ofResult { personId ->
            try {
                FetcherResult.Data(tmdbApi.getPersonImages(personId))
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Throwable) {
                ex.printStackTrace()
                FetcherResult.Error.Exception(ex)
            }
        },
        sourceOfTruth = SourceOfTruth.of<Int, PersonImageDto, PersonImageEntity>(
            reader = { personId ->
                personImageDao.readSingle(personId) // flow
            },
            writer = { _, dto ->
                personImageDao.insertSingleElement(dto.toEntity())
            }
        )
    ).build()


    fun getPersonImageFlow(personTmdbId: Int): Flow<Map<String, String>> {
        return personImageStore.stream(
            StoreRequest.cached(key = personTmdbId, refresh = true)
        )
            .filterNot {
                it is StoreResponse.Loading || it is StoreResponse.NoNewData
            }
            .map { response ->
                val bestPoster = response.requireData()
                    .profiles
                    .filterNotUhdPosters()
                    .maxByOrNull { it.voteCount }
                    ?.filePath

                mapOf(
                    POSTER_KEY to "$TMDB_LINK$bestPoster"
                )
            }
    }


}


