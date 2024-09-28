package com.example.muvitracker.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import java.text.SimpleDateFormat
import java.util.Locale

 // fragment extended property for scope
 val Fragment.fragmentViewLifecycleScope :LifecycleCoroutineScope
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



fun Double.firstDecimalApproxToString(): String {
    return String.format("%.1f", this)
}

fun Float.firstDecimalApproxToString(): String {
    return String.format("%.1f", this)
    // % - numbers
    // .1f - decimal
}




