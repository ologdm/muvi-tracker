package com.example.muvitracker.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import java.text.SimpleDateFormat
import java.time.LocalDate
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
    return String.format("%.1f", this)
}

fun Float.firstDecimalApproxToString(): String {
    return String.format("%.1f", this)
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


// calculate person age
fun calculateAge(birthDate: String?, deathDate: String?): Int {
    // caso limite manca birthday o entrambi
    if (birthDate.isNullOrEmpty() || (birthDate.isNullOrEmpty() && deathDate.isNullOrEmpty()) ){
        return 0
    }


    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateOfBirth = LocalDate.parse(birthDate, formatter)
    if (deathDate.isNullOrEmpty()) { // e vivo
        val currentDate = LocalDate.now()
        return Period.between(dateOfBirth, currentDate).years
    } else { // è morto
        val deathDate = LocalDate.parse(deathDate, formatter)
        return Period.between(dateOfBirth, deathDate).years
    }


}




