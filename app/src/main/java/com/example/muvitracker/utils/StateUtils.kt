package com.example.muvitracker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.muvitracker.R


data class StateContainer<T>(
    var isNetworkError: Boolean = false,
    var isOtherError: Boolean = false,
    var data: T? = null
)


fun <T> StateContainer<T>.statesFlow(
    progressBar: ProgressBar?,
    errorTextview: TextView,
) {
    val context = errorTextview.context

    when {
        isNetworkError -> {
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_no_internet)
            println("XXX  ES NET ERROR")
        }

        isOtherError -> {
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_other)
            println("XXX  ES OTHER ERROR")
        }

        else -> { // implicit success
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.GONE
            println("XXX ES SUCCESS(ELSE)")
        }
    }
}
