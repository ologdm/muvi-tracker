package com.example.muvitracker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.MyApp
import com.example.muvitracker.R
import kotlin.Boolean


data class StateContainerThree<T>(
    var data: T? = null,
    var isNetworkError: Boolean = false,
    var isOtherError: Boolean = false
)


// 1.1.3 specifica per detail fragmen - OK

/*
    - Loading iniziale → mostra solo ProgressBar.
    - Dati disponibili (anche se no internet) → mostra mainLayoutDetail e chiudi errorTextView / ProgressBar.
    - Nessun dato + errore → mostra solo TextView di errore, mainLayoutDetail nascosto.
    - Passi una lambda bindData per aggiornare subito la UI con i dati disponibili.
 */

fun <T> StateContainerThree<T>.statesFlowDetail(
    errorTextview: TextView,
    progressBar: ProgressBar,
    mainLayout: ConstraintLayout,
    bindData: ((T) -> Unit)? = null
) {
    val context = MyApp.appContext

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
                errorTextview.text = " " + context.getString(R.string.error_message_no_internet) + " "
            }
        }


        // 2. no data, no connection
        isNetworkError && data == null -> {
            // No internet e nessun dato: mostra solo errore
            progressBar.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = " " + context.getString(R.string.error_message_no_internet) + " "
            mainLayout.visibility = View.GONE
        }

        // 3.
        isOtherError && data == null -> {
            // Altro errore e nessun dato
            progressBar.visibility = View.GONE
            errorTextview.visibility = View.VISIBLE
            errorTextview.text = " " + context.getString(R.string.error_message_other) + " "
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


// CASO  SEMPLIFICATO ---------------------------------------------------------------------------------
// 1. PER DATA
// data | data = null
// errore da skippare
data class StateContainerTwo<T>(
    var data: T? = null,
    var isError: Boolean = false
)


fun <T> StateContainerTwo<T>.statesFlow(
    progressBar: ProgressBar,
    errorTextview: TextView,
    errorMsg: String,
    recyclerView : RecyclerView,
    bindData: ((T) -> Unit)?
) {
    progressBar.visibility = View.VISIBLE

    if (data != null) { // data
        bindData?.invoke(data!!)
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorTextview.visibility = View.GONE
    } else {            // no data
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorTextview.visibility = View.VISIBLE
        errorTextview.text = errorMsg
    }
}


// 2. PER LISTE
// data | no data=> sempre emptylist
// stato internet non mi interessa
// 2 casi da gestire -> data| emptylist
// -> store deve restituire emptylist pero

data class ListStateContainerTwo<T>(
    var data: List<T>,
    var isError: Boolean = false
)

fun <T> ListStateContainerTwo<T>.twoStatesFlow(
    progressBar: ProgressBar,
    errorTextview: TextView,
    recyclerView: RecyclerView,
    errorMsg: String,
    bindData: ((List<T>) -> Unit)
) {
    progressBar.visibility = View.VISIBLE

    if (data.isNotEmpty()) { // data
        bindData.invoke(data)
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorTextview.visibility = View.GONE
    } else { // emptyList
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorTextview.visibility = View.VISIBLE
        errorTextview.text = errorMsg
    }
}



