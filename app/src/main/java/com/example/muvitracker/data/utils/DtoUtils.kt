package com.example.muvitracker.data.utils

import android.content.Context
import com.example.muvitracker.data.dto.movie.detail.VideosResult
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


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
 *  "one, two, three" -> ["one", "two", "three"]
 */
fun String.splitToCleanList(): List<String>? {
    return this
        .takeIf { it.isNotBlank() } // va avanti solo se stringa non vuota
        ?.split(",") // crea lista
        ?.map { it.trim() } // rimuove eventuali spazi da ogni elemento
        ?.filter { it.isNotBlank() } // rimuove eventuali stringhe vuote
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



// TODO 1.1.4 OTTIMIZZARE -----------------------------------------------------------------------------------------

/**
 * Translates the given text (assumed to be in English) into the system language using Ai ML Kit.
 * Returns the original text if translation fails or the text is null/blank.
 */
suspend fun translateToSystemLanguage(context: Context, text: String?)
        : String? {
    val clean = text?.trim()
    // Se è null ritorni null (non ha senso tradurre "null")
    if (clean == null) return null

    val targetLanguage = TranslateLanguage.fromLanguageTag(
        android.os.LocaleList.getDefault()[0].language
    ) ?: TranslateLanguage.ENGLISH // fallback

    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(targetLanguage)
        .build()

    val translator: Translator = Translation.getClient(options)

    return suspendCancellableCoroutine { cont ->
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        cont.resume(translatedText)
                    }
                    .addOnFailureListener {
                        cont.resume(text) // fallback: original text
                    }
            }
            .addOnFailureListener {
                cont.resume(text) // fallback: original text
            }
    }
}
















