package com.example.muvitracker.mainactivity.kotlin.popu

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
import com.example.muvitracker.mainactivity.kotlin.MainNavigatorK
import com.example.muvitracker.repo.kotlin.dto.PopuDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.EmptyStatesManagement

// kotlin
// 1) R.layout.fragment_popular ==> .fragment_popular ==> package level propoerty
// 2) recyclerView.layoutManager ==> .layoutManager ==> syntethic extension property
// 3) setter -   x.setElemento(y) ==> istanza.elemento = y

// modificato su main Fragment OK


class PopuFragment
    : Fragment(), PopuContract.View {


    // 1 ATTRIBUTI
    // 1.1 dichiaro
    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar
    private lateinit var retryButton: Button
    private lateinit var errorMsgTextview: TextView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // 1.2 inizializzo
    private val adapter = PopuAdapter()
    private val navigator = MainNavigatorK()

    private val presenter: PopuContract.Presenter = PopuPresenter(this)


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
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        // empty states
        progressBar = view.findViewById(R.id.progressBar)
        retryButton = view.findViewById(R.id.retryButton)
        errorMsgTextview = view.findViewById(R.id.errorMessageTextview)


        // swipe refresh
        // 1)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {

            presenter.serverCallAndUpdate(forceRefresh = true)
            // nascondo progress bar - in presenter OK
        }
        // 2) swipeRefreshLayout
        // poi nascondi caricamento di swipeRefresh: .setRefreshing(false); '
        // caso success - in .updateUi ()
        // caso error - in caso .setErrorPage(nointernet)



        // OK - movieId lo lascio solo per chiarezza
        adapter.setCallbackVH { movieId ->
            presenter.onVHolderCLick(movieId)
        }


        // default
        presenter.serverCallAndUpdate(forceRefresh = false)


        retryButton.setOnClickListener {
            presenter.serverCallAndUpdate(forceRefresh = false)
        }


    }


    // CONTRACT METHOD - OK

    // OK
    override fun UpdateUi(list: List<PopuDto>) {
        adapter.updateList(list)
    }


    // OK
    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {
        // chiamo funzione gestione stati
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            recyclerView,
            progressBar,
            retryButton,
            errorMsgTextview
        )

        // stop refreshing solo in questi 3 stati
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> swipeRefreshLayout.isRefreshing = false

            else -> {}  // non fare nulla
        }
    }


    // OK
    override fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(requireActivity(), movieId)
    }
}

