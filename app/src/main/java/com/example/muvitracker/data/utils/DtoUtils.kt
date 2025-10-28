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
 * priorità:
 *    1. trailer ufficiali,
 *    2. trailer con risoluzione > 720p
 *    3. se return null -> prenderà trailer in inglese da trakt
 */
fun VideosResult.youtubeLinkTransformation(): String? {
    // TODO eugi sistemare
//    results.filter { it.official }
//        .sortedByDescending { it.size }
//        .sortedByDescending(compareValues({ it.nonme }, { it.cogme }))
//        .firstOrNull()

    val video = results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && it.official } // 1.priorita official, anche
        ?: results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && it.size.toInt()>720 } // 2. priorità trailer
//            ?: results.firstOrNull { it.site == "YouTube" } // 3. fallback a qualsiasi YouTube
    // else prende quello di trakt in inglese

    return video?.key?.let { "https://www.youtube.com/watch?v=${it}" }
}