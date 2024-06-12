package com.example.muvitracker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.muvitracker.R


data class StateContainer<T>(
    val isNetworkError: Boolean = false,
    val isOtherError: Boolean = false,
    val data: T? = null
)


fun <T> StateContainer<T>.statesFlow(
    progressBar: ProgressBar,
    errorMsg: TextView,
) {
    val context = errorMsg.context

    when {
        isNetworkError -> {
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_no_internet)
            println("XXX  ES NET ERROR")
        }

        isOtherError -> {
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_other)
            println("XXX  ES OTHER ERROR")
        }

        else -> { // implicit success
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.GONE
            println("XXX ES SUCCESS(ELSE)")
        }
    }
}



