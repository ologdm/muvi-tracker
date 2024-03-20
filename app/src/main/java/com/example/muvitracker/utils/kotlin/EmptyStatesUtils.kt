package com.example.muvitracker.utils.kotlin

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.repo.kotlin.dto.DetaDto

/** contiene:
 * - EmptyStatesManagement class
 *              - emptyStatesFlow rv e frame
 *              - fun setProgressBar
 *              - setErrorPage
 *              - setFrameVisibility
 *
 * EmptyStates enum
 *
 * Visibility enum
 *
 * EmptyStatesCallback interface - programmazione assincrona
 *      > passa stati al presenter
 *      > onSuccess (passa dato)
 *
 *
 */



object EmptyStatesManagement {

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

    // versione RV
    fun emptyStatesFlow(
        emptyStates: EmptyStatesEnum,
        recycleView: RecyclerView,
        progressBar: View,
        retryButton: Button,
        errorMsgTextview: TextView
    ) {
        // when e esaustivo, vuole tutti i casi
        when (emptyStates) {

            EmptyStatesEnum.ON_START -> {
                setRvVisibility(recycleView, Visibility.HIDE)
                setProgressBar(progressBar, Visibility.SHOW)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_FORCE_REFRESH -> {
                setRvVisibility(recycleView, Visibility.HIDE)
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


    // versione FrameLayout OK
    fun emptyStatesFlow(
        emptyStates: EmptyStatesEnum,
        frame: View,
        progressBar: View,
        retryButton: Button,
        errorMsgTextview: TextView
    ) {
        // when e esaustivo, vuole tutti i casi
        when (emptyStates) {

            EmptyStatesEnum.ON_START -> {
                setFrameVisibility(frame, Visibility.HIDE)
                setProgressBar(progressBar, Visibility.SHOW)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_FORCE_REFRESH -> {
                setFrameVisibility(frame, Visibility.HIDE)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_SUCCESS -> {
                setFrameVisibility(frame, Visibility.SHOW)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.HIDE)
            }

            EmptyStatesEnum.ON_ERROR_IO -> {
                setFrameVisibility(frame, Visibility.HIDE)
                setProgressBar(progressBar, Visibility.HIDE)
                setErrorPage(retryButton, errorMsgTextview, Visibility.SHOW, NO_INTERNET_MSG)
            }

            EmptyStatesEnum.ON_ERROR_OTHER -> {
                setFrameVisibility(frame, Visibility.HIDE)
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


    private fun setFrameVisibility(
        frame: View,
        visibilita: Visibility
    ) {

       frame.visibility =
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


interface EmptyStatesCallback {
    fun OnStart()
    fun onSuccess (detaDto: DetaDto)
    fun onErrorIO ()
    fun onErrorOther ()
}




