package com.example.muvitracker.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

// fragment extended property for scope
val Fragment.fragmentViewLifecycleScope: LifecycleCoroutineScope
    get() = this.viewLifecycleOwner.lifecycleScope


// dateFormatter ###############################################################
// sdk 24 compatible
fun String.dateFormatterInMMMyyy(): String {
    val formatterInput = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)   // Input date format
    val formatterOutput = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)    // Output date format

    val date = formatterInput.parse(this)     // Parse the input date string
    return formatterOutput.format(date)              // Format to output string
}

// sdk 26
//    private fun dateFormatter(data: String): String {
//        // style input
//        val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        // style output
//        val formatterOutput = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
//        // save input
//        val dataLocale = LocalDate.parse(data, formatterInput)
//        // modify to output
//        return dataLocale.format(formatterOutput)
//    }

fun String.dateFormatterInddMMMyyy(): String {
    val formatterInput = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)   // Input date format
    val formatterOutput = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)    // Output date format

    val date = formatterInput.parse(this)     // Parse the input date string
    return formatterOutput.format(date)              // Format to output string
}


fun Double.firstDecimalApproxToString(): String {
    return String.format(Locale.US, "%.1f", this)
}

fun Float.firstDecimalApproxToString(): String {
    return String.format(Locale.US, "%.1f", this)
    // % - numbers
    // .1f - decimal
}


fun Int.episodesFormatNumber(): String {
    return if (this < 10) {
        // Aggiungi zero davanti ai numeri a una cifra
        String.format("%02d", this)
    } else {
        this.toString()
    }
}


// return: 0 or deathAge or currentAge
fun calculatePersonAge(birthDate: String?, deathDate: String?): String {
    // 1) edge case: missing birthDate or both
    if (birthDate.isNullOrEmpty() ||
        (birthDate.isNullOrEmpty() && deathDate.isNullOrEmpty())
    ) {
        return "N/A"
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateOfBirth = LocalDate.parse(birthDate, formatter)

    if (deathDate.isNullOrEmpty()) { // 2) alive
        val currentDate = LocalDate.now()
        return Period.between(dateOfBirth, currentDate).years.toString()
    } else { // 3) death
        val deathDate = LocalDate.parse(deathDate, formatter)
        return Period.between(dateOfBirth, deathDate).years.toString()
    }
}


fun String?.formatDateFromFirsAired(): String {
    return this?.let {
        if (it.length >= 10) {
            "${it.substring(8, 10)}.${it.substring(5, 7)}.${it.substring(0, 4)}"
        } else {
            "N/A"
        }
    } ?: "N/A"
}


fun String?.getDateFromDateTime(): String? {
    return this?.takeIf { it.isNotBlank() }
        ?.substring(0, 10)
}


// data corrente in formato compatibile con "yyyy-MM-dd HH:mm:ss" di SQLite
fun getNowFormattedDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val nowFormatted = LocalDateTime.now().format(formatter)
    return nowFormatted
}


// per episode
// data in formato compatibile con "yyyy-MM-dd HH:mm:ss" di SQLite
fun formatToSqliteCompatibleDate(isoDate: String?): String? {
    if (isoDate.isNullOrBlank()) return null

    return if (isoDate.length == 10) {
        // Caso solo data, es: "2025-12-19"
        "$isoDate 00:00:00"
    } else {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // Caso ISO con T e Z, es: "2011-04-25T01:00:00.000Z"
        OffsetDateTime.parse(isoDate)
//            .withOffsetSameInstant(ZoneOffset.UTC)
//            .toLocalDateTime()
            .format(formatter)
    }
}


// input -> Data + ora locale (yyyy-MM-dd'T'HH:mm:ss)
//fun String?.formatToReadableDate(): String? {
//    return this.let {
//        val dt =
//            LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//        dt.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("en")))
//    }
//}


// TODO 1.1.3 OK
// input - Solo data (yyyy-MM-dd)
fun String?.formatToReadableDate(): String? {
    if (this.isNullOrBlank()) return null
    return try {
        val dt = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        // Usa la lingua salvata o la lingua di sistema corrente
        val locale = Locale.getDefault() // oppure LanguageManager.getSystemLanguage()
        dt.format(DateTimeFormatter.ofPattern("dd MMM yyyy", locale))
    } catch (e: Exception) {
        null
    }
}


// ---------------------------------------

// usato per defaults -> in detail fragment, prefs ecc
fun String?.orIfNullOrBlank(fallback : String): String {
   return if (!this.isNullOrBlank()) this else fallback
}









