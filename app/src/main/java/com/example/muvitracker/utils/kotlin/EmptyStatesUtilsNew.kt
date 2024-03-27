package com.example.muvitracker.utils.kotlin

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** contiene:
 * - EmptyStatesManagementNew class

 *
 * EmptyStates enum
 *
 * Visibility enum

 *
 */


object EmptyStatesManagementNew {

    // non serve privato
    const val NO_INTERNET_MSG = "no internet connection, swipe down"
    const val OTHER_ERROR_MSG = "something went wrong"


    // OK
    /** elementi necessari:
     *  - ProgressBar,
     *  - ErrorPage,
     *  - RecycleView
     *
     *  stati:
     *  due iniziali
     *  un success
     *  due error
     */


    /** nuovo setup
    ON_START,                 // local + start loading + no error
    ON_FORCE_REFRESH,         // local, no loading, no error
    ON_SUCCESS,               // server + stop loading + no error
    ON_ERROR_IO,              // local + stop loading + error msg
    ON_ERROR_OTHER            // local + stop loading + error msg
     */

    // versione NUOVA - dati in locale
    fun emptyStatesFlow(
        emptyStates: EmptyStatesEnumNew,
        progressBar: View,
        errorMsgTextview: TextView
    ) {
        when (emptyStates) {

            EmptyStatesEnumNew.ON_START -> {
                setProgressBar(progressBar, Visibility.SHOW) // si, da mettere quello lineare
                setErrorMsg(errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnumNew.ON_FORCE_REFRESH -> {
                setProgressBar(progressBar, Visibility.HIDE) //no
                setErrorMsg(errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnumNew.ON_SUCCESS -> {
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorMsg(errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnumNew.ON_ERROR_IO -> {
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorMsg(errorMsgTextview, Visibility.SHOW, NO_INTERNET_MSG)
            }

            EmptyStatesEnumNew.ON_ERROR_OTHER -> {
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorMsg(errorMsgTextview, Visibility.SHOW, OTHER_ERROR_MSG)
            }

        }
    }


    /** funzioni private -> utilizzate dentro
     *
     */
    // 1. OK nuovo
    private fun setProgressBar(
        progressBar: View,
        visibilita: Visibility
    ) {

        progressBar.visibility =
            if (visibilita == Visibility.SHOW)
                View.VISIBLE
            else
                View.GONE
    }


    // 2. OK nuovo
    //  ErrorMsg solo
    private fun setErrorMsg(
        errorMsgTextview: TextView,
        visibilita: Visibility,
        errorMsg: String = ""
    ) {
        errorMsgTextview.visibility =
            if (visibilita == Visibility.SHOW)
                View.VISIBLE
            else
                View.GONE

        errorMsgTextview.setText(errorMsg)
    }


    /* non serve
    private fun setRvVisibility(
        recycleview: RecyclerView,
        visibilita: Visibility
    ) {

        recycleview.visibility =
            if (visibilita == Visibility.SHOW) View.VISIBLE
            else View.GONE

    }

     */


    private enum class Visibility {
        SHOW,
        HIDE
    }


}


enum class EmptyStatesEnumNew {
    ON_START, // local + start loading + no error
    ON_FORCE_REFRESH, // local, no loading, no error
    ON_SUCCESS, // server + stop loading + no error
    ON_ERROR_IO, // local + stop loading + error msg
    ON_ERROR_OTHER // // local + stop loading + error msg
}


/*
private fun setFrameVisibility(
    frame: View,
    visibilita: Visibility
) {

    frame.visibility =
        if (visibilita == Visibility.SHOW) View.VISIBLE
        else View.GONE

}

 */









