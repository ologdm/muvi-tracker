package com.example.muvitracker.mainactivity.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.muvitracker.R
import com.example.muvitracker.mainactivity.MainNavigatorK
import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.EmptyStatesManagement

// kotlin
// 1) R.layout.fragment_popular ==> .fragment_popular ==> package level propoerty
// 2) recyclerView.layoutManager ==> .layoutManager ==> syntethic extension property
// 3) setter -   x.setElemento(y) ==> istanza.elemento = y

// TODO modificare su main Fragment

@Suppress("UNUSED")
class PopularFragmentK : Fragment(), PopularContractK.View {


    // 1 ATTRIBUTI
    // 1.1 dichiaro
    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar
    private lateinit var retryButton: Button
    private lateinit var errorMessage: TextView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // 1.2 inizializzo
    private val adapter = PopularAdapterK()
    private val navigator = MainNavigatorK()

    private val presenter: PopularContractK.Presenter = PopularPresenterK(this)


    // 2 METODI FRAGMENT

    // 2.1 creazione view - OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }


    // 2.2 logica
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // rv
        recyclerView = view.findViewById(R.id.popularFragmentRV)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)
        recyclerView.adapter = adapter

        // empty states
        progressBar = view.findViewById(R.id.progressBar)
        retryButton = view.findViewById(R.id.retryButton)
        errorMessage = view.findViewById(R.id.errorMessageTextview)


        // swipe refresh
        // 1)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {

            presenter.serverCallAndUpdate(forceRefresh = true)
            // nascondo progress bar - in presenter OK
        }

        // 2)
        // poi nascondi caricamento di swipeRefresh: .setRefreshing(false); '
        // caso success - in .updateUi ()
        // caso error - in caso .setErrorPage(nointernet)


        // OK - movieId lo lascio solo per chiarezza
        adapter.setCallbackVH { movieId ->
            presenter.onVHolderCLick(movieId)
        }


        // default
        presenter.serverCallAndUpdate(forceRefresh = false)

    }


    // CONTRACT METHOD - OK

    // OK
    override fun UpdateUi(list: List<PopularDtoK>) {
        adapter.updateList(list)
    }

    // OK
    override fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(requireActivity(), movieId)
    }

    // OK
    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {
        // chiamo funzione gestione stati
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            recyclerView,
            progressBar,
            retryButton,
            errorMessage
        )

        // stop refreshing solo in questi 3 stati
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> swipeRefreshLayout.isRefreshing = false
            else -> {}
        }


    }
}

