package com.example.muvitracker.mainactivity.kotlin.boxo

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
import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.EmptyStatesManagement


// su main OK


class BoxoFragment : Fragment(), BoxoContract.View {

    // dichiaro OK
    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar
    private lateinit var retryButton: Button
    private lateinit var errorMsgTextview: TextView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // inizializzo

    private val adapter = BoxoAdapter()
    private val presenter = BoxoPresenter(this) //richiede qualcuno che implementa Contract.View
    private val navigator = MainNavigatorK()


    // crea OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_boxo, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState) // i paramentri gli passo al costruttore

        recyclerView = view.findViewById(R.id.boxofficeFragmentRV)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        // TODO
        progressBar = view.findViewById(R.id.progressBar)
        retryButton = view.findViewById(R.id.retryButton)
        errorMsgTextview = view.findViewById(R.id.errorMsgTextview)

        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)

        swipeRefreshLayout.setOnRefreshListener { // ()->Unit, interfaccia parametro vuoto

            // implementazione OnRefreshListener
            presenter.serverCallAndUpdateUi(forceRefresh = true)

        }


        presenter.serverCallAndUpdateUi(forceRefresh = false)

        adapter.setcallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
        }


        retryButton.setOnClickListener {
            presenter.serverCallAndUpdateUi(forceRefresh = false)
        }


    }


    // CONTRACTS METHODS

    override fun updateUi(list: List<BoxoDto>) {
        adapter.updateList(list)
    }


    override fun emptyStatesManagment(emptyState: EmptyStatesEnum) {

        //  set funzione gestione stati
        EmptyStatesManagement.emptyStatesFlow(
            emptyState,
            recyclerView,
            progressBar,
            retryButton,
            errorMsgTextview
        )

        // gestione swipe refresh -> fine

        when (emptyState) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> swipeRefreshLayout.isRefreshing = false

            else -> {} // non fare nulla
        }
    }


    // OK
    override fun startDetailsFragment(traktMovieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            traktMovieId
        )
    }

}