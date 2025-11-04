package com.example.muvitracker.data.utils

import com.example.muvitracker.data.dto.movie.detail.VideosResult


//
fun <T> coalesce(value: T?, fallback: T?): T? {
    return when (value) {
        is String -> if (value.isNotBlank()) value else fallback
        is Collection<*> -> if (!value.isNullOrEmpty()) value else fallback
        else -> value ?: fallback
    }
}


// CON EXTENTION FUNCTION - scegli tra 2 opzioni
// Per String nullable
fun String?.orIfBlank(fallback: String?): String? {
    return if (!this.isNullOrBlank()) this else fallback
}

// Per Collection nullable
//fun <T> Collection<T>?.orIfEmpty(fallback: Collection<T>?): Collection<T>? {
//    return if (!this.isNullOrEmpty()) this else fallback
//}

// Per List nullable
fun <T> List<T>?.orIfEmpty(fallback: List<T>?): List<T>? {
    return if (!this.isNullOrEmpty()) this else fallback
}

// Per tipi generici nullable
fun <T> T?.orIfNull(fallback: T?): T? {
    return this ?: fallback
}


/**
 * Priority:
 *  1. Official YouTube trailers
 *  2. Fallback: YouTube trailers with resolution > 720p
 *  3. Final fallback (if null): fetch English trailer from Trakt (on movie or show DTO)
 */
fun VideosResult.youtubeLinkTransformation(): String? {
    val video =
        // 1 official
        this.results.filter { it.official && it.type == "Trailer" && it.site == "YouTube" }
            .maxByOrNull { it.size } //
        // 2 fallback
            ?: results.filter { it.type == "Trailer" && it.site == "YouTube" && it.size.toInt() > 720 }
                .maxByOrNull { it.size } //

    return video?.key?.let { "https://www.youtube.com/watch?v=${it}" }
}
















