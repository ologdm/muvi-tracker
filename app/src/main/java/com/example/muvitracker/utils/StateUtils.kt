package com.example.muvitracker.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.muvitracker.R


data class StateContainer<T>(
    val isNetworkError: Boolean = false,
    val isOtherError: Boolean = false,
    val dataList: List<T> = emptyList(),
    val data: T? = null // details // null management
)


fun <T> StateContainer<T>.statesFlow(
//    progressBar: ProgressBar,
    errorMsg: TextView,
    insideScrollView: ViewGroup? = null,
//    view: View? = null,  todo test con view
) {
    val context = errorMsg.context

    when {
        isNetworkError -> { // OK
//            progressBar.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_no_internet)
//            insideScrollView?.visibility = View.GONE
            println("XXX  ES NET ERROR")
        }

        isOtherError -> { // OK == network, different msg
//            progressBar.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_other)
//            insideScrollView?.visibility = View.GONE
            println("XXX  ES OTHER ERROR")
        }

        else -> { // implicit success OK
//            progressBar.visibility = View.GONE
            errorMsg.visibility = View.GONE
//            insideScrollView?.visibility = View.VISIBLE
            println("XXX ES SUCCESS(ELSE)")
        }
    }
}

fun Boolean.loading(progressBar: ProgressBar) {
    when (this) {
        true -> {
            progressBar.visibility = View.VISIBLE
        }

        false -> {
            progressBar.visibility = View.GONE
        }
    }
}


//isLoading -> { // OK
//    progressBar.visibility = View.VISIBLE
//    errorMsg.visibility = View.GONE
//    insideScrollView?.visibility = View.GONE
//    println("XXX  ES LOADING")
//}
//
//isRefresh -> { // OK
//    progressBar.visibility = View.GONE
//    errorMsg.visibility = View.GONE
//    insideScrollView?.visibility = View.GONE
//    println("XXX  ES REFRESH")
//}

/* OK
// per entrambe fatto check OK
// controllo su:
// - progress bar
// - error message
// - viewGroup o view (only for details) -> if null don't do nothing (ck eugi)
// (View padre di Viewgroup e tutte le view)
 */


