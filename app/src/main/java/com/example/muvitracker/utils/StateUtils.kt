package com.example.muvitracker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.muvitracker.R


data class StateContainer<T>(
    var data: T? = null,
    var isNetworkError: Boolean = false,
    var isOtherError: Boolean = false
)


fun <T> StateContainer<T>.statesFlow(
    errorTextview: TextView,
    progressBar: ProgressBar?
) {
    val context = errorTextview.context

    when {
        isNetworkError -> {
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_no_internet_swipe_down)
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


fun <T> StateContainer<T>.statesFlow1(
    errorTextview: TextView,
    progressBar: ProgressBar?
) {
    val context = errorTextview.context

    when {
        isNetworkError -> {
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_no_internet)
        }

        isOtherError -> {
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_other)
        }

        else -> { // implicit success
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.GONE
        }
    }
}

