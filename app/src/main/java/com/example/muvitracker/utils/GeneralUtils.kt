package com.example.muvitracker.utils

import java.text.SimpleDateFormat
import java.util.Locale


fun Double.firstDecimalApproxToString(): String {
    return String.format("%.1f", this)
}

fun Float.firstDecimalApproxToString(): String {
    return String.format("%.1f", this)
    // % - numero
    // .1f - numero decimali
}


// dateFormatter #########################
// sdk 24 compatibile
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

