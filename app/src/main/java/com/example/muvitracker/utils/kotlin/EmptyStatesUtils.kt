package com.example.muvitracker.utils.kotlin

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** contiene:
 * - Management
 * - EmptyStates -Enum
 * - Visibilita - Enum
 *
 */



object EmptyStatesManagement {

    // non serve privato
    const val NO_INTERNET_MSG = "no internet connection PROVA"
    const val OTHER_ERROR_MSG = "something went wrong"


    // OK
    /** elementi necessari:
     *  - ProgressBar,
     *  - ErrorPage,
     *  - RecycleView
     *
     *  stati:
     *  2 iniziali
     *  1 success
     *  2 error
     */
    fun emptyStatesFlow(
        emptyStates: EmptyStatesEnum,
        recycleView: RecyclerView,
        progressBar: View,
        retryButton: Button,
        errorMsgTextview: TextView
    ) {

        when (emptyStates) {

            EmptyStatesEnum.ON_START -> {
                setRvVisibility(recycleView, Visibility.SHOW)
                setProgressBar(progressBar, Visibility.SHOW)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_FORCE_REFRESH -> {
                setRvVisibility(recycleView, Visibility.SHOW)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_SUCCESS -> {
                setRvVisibility(recycleView, Visibility.SHOW)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_ERROR_IO -> {
                setRvVisibility(recycleView, Visibility.HIDE)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.SHOW, NO_INTERNET_MSG)
            }

            EmptyStatesEnum.ON_ERROR_OTHER -> {
                setRvVisibility(recycleView, Visibility.HIDE)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.SHOW, OTHER_ERROR_MSG)
            }

        }
    }

    /** funzioni private -> utilizzate dentro
     *
     */
    // 1. OK
    private fun setProgressBar(
        progressBar: View,
        visibilita: Visibility
    ) {

        progressBar.visibility =
            if (visibilita == Visibility.SHOW) View.VISIBLE
            else View.GONE
    }


    // 2. OK
    /**  setErrorPage - button e message sempre insieme */
    private fun setErrorPage(
        retryButton: Button,
        errorMessageTextView: TextView,
        visibilita: Visibility,
        errorMsg: String = ""
    ) {

        retryButton.visibility =
            if (visibilita == Visibility.SHOW) View.VISIBLE
            else View.GONE

        errorMessageTextView.visibility =
            if (visibilita == Visibility.SHOW) View.VISIBLE
            else View.GONE

        errorMessageTextView.setText(errorMsg)
    }


    // 3. OK
    private fun setRvVisibility(
        recycleview: RecyclerView,
        visibilita: Visibility
    ) {

        recycleview.visibility =
            if (visibilita == Visibility.SHOW) View.VISIBLE
            else View.GONE

    }

}


enum class EmptyStatesEnum {
    ON_START,
    ON_FORCE_REFRESH,
    ON_SUCCESS,
    ON_ERROR_IO,
    ON_ERROR_OTHER
}


private enum class Visibility {
    SHOW,
    HIDE
}




