package com.example.muvitracker.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.muvitracker.R


data class StateContainer<T>(
    val dataList: List<T> = emptyList(),
    var data: T? = null, // details // null management
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isNetworkError: Boolean = false,
    val isOtherError: Boolean = false
)

/* OK
// per entrambe fatto check OK
// controllo su:
// - progress bar
// - error message
// - viewGroup o view (only for details) -> if null don't do nothing (ck eugi)
// (View padre di Viewgroup e tutte le view)
 */


fun <T> StateContainer<T>.statesFlow(
    progressBar: ProgressBar,
    errorMsg: TextView,
    insideScrollView: ViewGroup? = null,
//    view: View? = null,  todo test con view
) {
    val context = progressBar.context

    when {
        isLoading -> { // OK
            progressBar.visibility = View.VISIBLE
            errorMsg.visibility = View.GONE
            insideScrollView?.visibility = View.GONE
        }

        isRefresh -> { // OK
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.GONE
            insideScrollView?.visibility = View.GONE
        }

        isNetworkError -> { // OK
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_no_internet)
            insideScrollView?.visibility = View.GONE
        }

        isOtherError -> { // OK == network, different msg
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = context.getString(R.string.error_message_other)
            insideScrollView?.visibility = View.GONE
        }

        else -> { // implicit success OK
            progressBar.visibility = View.GONE
            errorMsg.visibility = View.GONE
            insideScrollView?.visibility = View.VISIBLE
        }
    }
}



