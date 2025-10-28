package com.example.muvitracker.data.dto.movie.detail

import com.example.muvitracker.data.LanguageManager
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.data.utils.orIfBlank
import com.example.muvitracker.data.utils.orIfEmpty
import com.example.muvitracker.data.utils.orIfNull
import com.example.muvitracker.data.utils.youtubeLinkTransformation
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.google.gson.annotations.SerializedName


// only base + fallback
// es: https://api.trakt.tv/movies/190430?extended=full
// traktDto - es deadpool - 190430

data class DetailMovieTraktDto(
    // base
    val title: String?, // Deadpool
    val year: Int?,  // 2016
    val ids: Ids,

    // extended
    val tagline: String?, // Feel the love
    val overview: String?, // The origin story of former Special Forces...
    val released: String?, // 2016-02-12
    val runtime: Int?, // 108
    val country: String?, // us
//    @SerializedName("updated_at") val updatedAt: String?,
    val trailer: String?, // https://youtube.com/watch?v=9vN6DHB6bJc
    val homepage: String?, // http://www.20thcenturystudios.com/movies/deadpool
    val status: String?, // released
    val rating: Float?,  // 8.25713 - trakt
//    val votes: Int?,     // 121407
//    @SerializedName("comment_count") val commentCount: Int?,
    val language: String?, // "en" - lingua originale
//    val languages: List<String>?, //en,de - multilingue originali
//    val availableTranslations: List<String>?,
    val genres: List<String>?, // action, thriller
//    val subgenres : List<String>?, // mercenary, based-on-comic, anti-hero
//    val certification: String?
    @SerializedName("original_title")
    val originalTitle: String? // Deadpool
)


/**
 * Unisce i dati provenienti da due sorgenti (Trakt e TMDB) in un'unica entità [DetailMovieEntity].
 *
 * ## Logica di unione
 * 1. **Base Trakt:** i campi provenienti da Trakt sono sempre presenti e rappresentano la base dati.
 * 2. **Priorità TMDB:** se TMDB fornisce un valore valido (non nullo e non vuoto), viene utilizzato al posto di quello Trakt.
 * 3. **Fallback:** se il campo TMDB è mancante, nullo o vuoto, viene utilizzato il corrispondente valore di Trakt.
 *
 * ## Note
 * - I valori provenienti da TMDB hanno priorità solo se contengono dati realmente disponibili o tradotti.
 * - Le liste (come `countries` e `genres`) vengono normalizzate e mai lasciate nulle.
 * - I voti (rating) non prevedono fallback: vengono presi separatamente da Trakt e TMDB.
 * - Il campo `releaseDate` mantiene il formato originale fornito dalle API (senza conversione SQLite).
 * - Il campo `currentTranslation` rappresenta la lingua di sistema corrente, utilizzata per identificare la traduzione attiva.
 *
 * @param trakt Oggetto [DetailMovieTraktDto] obbligatorio, che contiene i dati base provenienti da Trakt.
 * @param tmdb Oggetto [DetailMovieTmdbDto] opzionale, che fornisce dati aggiuntivi (es. traduzioni, immagini, generi).
 * @return Un oggetto [DetailMovieEntity] che combina in modo coerente le informazioni provenienti da entrambe le sorgenti.
 */
fun mergeMoviesDtoToEntity(
    trakt: DetailMovieTraktDto, tmdb: DetailMovieTmdbDto?
): DetailMovieEntity {
    return DetailMovieEntity(
        // trakt base
        traktId = trakt.ids.trakt,
        year = trakt.year,
        ids = trakt.ids,

        // tmdb (or trakt fallback)
        title = tmdb?.title.orIfBlank(trakt.title),
        tagline = tmdb?.tagline.orIfBlank(trakt.tagline),
        overview = tmdb?.overview.orIfBlank(trakt.overview),
        status = tmdb?.status.orIfBlank(trakt.status),
        // from trakt - "2016-02-12" | tmdbDto - "2016-02-09"
        releaseDate = tmdb?.releaseDate.orIfBlank(trakt.released), // NO formatToSqliteCompatibleDate()
        // from trakt - us  | tmdb - US,GB
        countries = tmdb?.originCountry
            .orIfEmpty(trakt.country?.takeIf { it.isNotBlank() }?.let { listOf(it.uppercase()) })
            ?: emptyList(), // OK
        runtime = tmdb?.runtime.orIfNull(trakt.runtime),
        originalLanguage = tmdb?.originalLanguage.orIfBlank(trakt.language),
        originalTitle = tmdb?.originalTitle.orIfBlank(trakt.originalTitle),
        genres = tmdb?.genres?.map { it.name }
            .orIfEmpty(trakt.genres)
            ?: emptyList(), // !! not empty
        youtubeTrailer = tmdb?.videos?.youtubeLinkTransformation().orIfBlank(trakt.trailer),
        homepage = tmdb?.homepage.orIfBlank(trakt.homepage),

        // solo tmdb
        backdropPath = tmdb?.backdropPath,
        posterPath = tmdb?.posterPath, // solo tmdb

        // voti (no fallback)
        traktRating = trakt.rating?.firstDecimalApproxToString(), // in 8.25713-> out 8.3
        tmdbRating = tmdb?.voteAverage?.firstDecimalApproxToString(), // in 8.458 -> out 8.5

        // lingua
        currentTranslation = LanguageManager.getSystemLocaleTag()
    )
}
