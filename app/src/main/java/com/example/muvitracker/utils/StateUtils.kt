package com.example.muvitracker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.muvitracker.R
import kotlin.Boolean


data class StateContainerThree<T>(
    var data: T? = null,
    var isNetworkError: Boolean = false,
    var isOtherError: Boolean = false
)


// TODO 1.1.3 - per cast, stagioni, ecc - non serve errore specifico
data class StateContainerTwo<T>(
    var data: T? = null,
    var isError: Boolean = false
)


// non si usa, ora quella di PagingState
//fun <T> StateContainer<T>.statesFlowOld(
//    errorTextview: TextView,
//    progressBar: ProgressBar?
//) {
//    val context = errorTextview.context
//
//    when {
//        isNetworkError -> {
//            progressBar?.visibility = View.GONE
//            errorTextview.visibility = View.VISIBLE
//            errorTextview.text = context.getString(R.string.error_message_no_internet_swipe_down)
//            println("XXX  ES NET ERROR")
//        }
//
//        isOtherError -> {
//            progressBar?.visibility = View.GONE
//            errorTextview.visibility = View.VISIBLE
//            errorTextview.text = context.getString(R.string.error_message_other)
//            println("XXX  ES OTHER ERROR")
//        }
//
//        else -> { // implicit success
//            progressBar?.visibility = View.GONE
//            errorTextview.visibility = View.GONE
//            println("XXX ES SUCCESS(ELSE)")
//        }
//    }
//}


// TODO eliminare
fun <T> StateContainerThree<T>.statesFlow(
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


// TODO 1.1.3 specifica per detail movie - modificata
fun <T> StateContainerThree<T>.statesFlowDetailNew(
    errorTextview: TextView,
    progressBar: ProgressBar,
    mainLayout: ConstraintLayout,
    bindData: ((T) -> Unit)? = null
) {
    val context = errorTextview.context


    // Controllo se ho un dato effettivo -> serve
//    val isDataReady = data != null

    when {
        // 1. success, data
        data != null -> {
            progressBar.visibility = View.GONE
            bindData?.invoke(data!!)
            mainLayout.visibility = View.VISIBLE

            // 1.1 Mostra il messaggio “No internet” se c'è errore di rete, ma senza nascondere i dati
            errorTextview.visibility = if (isNetworkError) View.VISIBLE else View.GONE
            if (isNetworkError) {
                errorTextview.text = context.getString(R.string.error_message_no_internet)
            }
        }


        // 2. no data, no connection
        isNetworkError && data == null -> {
            // No internet e nessun dato: mostra solo errore
            progressBar.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_no_internet)
            mainLayout.visibility = View.GONE
        }

        // 3.
        isOtherError && data == null -> {
            // Altro errore e nessun dato
            progressBar.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = context.getString(R.string.error_message_other)
            mainLayout.visibility = View.GONE
        }

        // 4. loading
        else -> { // caso no dato , no exception,
            // Loading iniziale
            progressBar.visibility = View.VISIBLE
            errorTextview.visibility = View.GONE
            mainLayout.visibility = View.GONE
        }
    }
}

/*
    - Loading iniziale → mostra solo ProgressBar.
    - Dati disponibili (anche se no internet) → mostra mainLayoutDetail e chiudi errorTextView / ProgressBar.
    - Nessun dato + errore → mostra solo TextView di errore, mainLayoutDetail nascosto.
    - Passi una lambda bindData per aggiornare subito la UI con i dati disponibili.
 */



//TODO 1.1.3 new per cast, stagioni, ecc - errore generico OK
fun <T> StateContainerTwo<T>.statesFlow(
    progressBar: ProgressBar?,
    errorTextview: TextView,
    errorMsg: String,
    bindData: ((T) -> Unit)?
) {
    val context = errorTextview.context
    // progressBar -> visibility="visible" di default

    when {
        data != null -> {
            bindData?.invoke(data!!)
            progressBar?.visibility = View.GONE
        }

        isError -> {
            progressBar?.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = errorMsg
        }

        else -> {  // caso:  data == null && isError == false
            progressBar?.visibility = View.VISIBLE
            errorTextview.visibility = View.GONE
        }
    }
}



