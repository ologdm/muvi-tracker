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
    errorMsg: TextView,
) {
    val context = errorMsg.context

    when {
        isNetworkError -> {
            progressBar?.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_no_internet)
            println("XXX  ES NET ERROR")
        }

        isOtherError -> {
            progressBar?.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_other)
            println("XXX  ES OTHER ERROR")
        }

        else -> { // implicit success
            progressBar?.visibility = View.GONE
            errorMsg.visibility = View.GONE
            println("XXX ES SUCCESS(ELSE)")
        }
    }
}


// ###################################################
//sealed class UiState<T> {
//    data object Loading : UiState<Nothing>()
//    data class Success<T>(val data: List<T>) : UiState<T>()
//    data class Error(val errorMsg: String?) : UiState<Nothing>()
//}
//
//fun <T> UiState<T>.statesFlow(
//    progressBar: ProgressBar,
//    errorTextView: TextView,
//) {
//    when (this) {
//        is UiState.Loading -> {
//            progressBar.visibility = View.VISIBLE
//            errorTextView.visibility = View.GONE
//        }
//
//        is UiState.Success -> {
//            progressBar.visibility = View.GONE
//            errorTextView.visibility = View.GONE
//        }
//
//        is UiState.Error -> {
//            progressBar.visibility = View.GONE
//            errorTextView.visibility = View.VISIBLE
//            errorTextView.text = this.errorMsg
//        }
//    }
//}


//data class StateContainerTest(
//    var isNetworkError: Boolean = false,
//    var isOtherError: Boolean = false,
//)
//
//fun StateContainerTest.statesFlow(
//    progressBar: ProgressBar,
//    errorMsg: TextView,
//) {
//    val context = errorMsg.context
//
//    when {
//        isNetworkError -> {
//            progressBar.visibility = android.view.View.GONE
//            errorMsg.visibility = android.view.View.VISIBLE
//            errorMsg.text = context.getString(com.example.muvitracker.R.string.error_message_no_internet)
//            println("XXX  ES NET ERROR")
//        }
//
//        isOtherError -> {
//            progressBar.visibility = android.view.View.GONE
//            errorMsg.visibility = android.view.View.VISIBLE
//            errorMsg.text = context.getString(com.example.muvitracker.R.string.error_message_other)
//            println("XXX  ES OTHER ERROR")
//        }
//
//        else -> { // implicit success
//            progressBar.visibility = android.view.View.GONE
//            errorMsg.visibility = android.view.View.GONE
//            println("XXX ES SUCCESS(ELSE)")
//        }
//    }



