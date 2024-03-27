package com.example.muvitracker.mainactivity.kotlin.popu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmPopuBinding
import com.example.muvitracker.mainactivity.kotlin.MainNavigatorK
import com.example.muvitracker.repo.kotlin.dto.PopuDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnumNew
import com.example.muvitracker.utils.kotlin.EmptyStatesManagementNew

// kotlin
// 1) R.layout.fragment_popular ==> .fragment_popular ==> package level propoerty
// 2) recyclerView.layoutManager ==> .layoutManager ==> syntethic extension property
// 3) setter -   x.setElemento(y) ==> istanza.elemento = y

// modificato su main Fragment OK


class PopuFragment
    : Fragment(), PopuContract.View {


    // binding dichiarazione OK
    private var binding: FragmPopuBinding? = null


    // TODO
    //private lateinit var recyclerView: RecyclerView

    //private lateinit var progressBar: ProgressBar
    //private lateinit var errorMsgTextview: TextView
    //private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    // 1 ATTRIBUTI
    // 1.1 dichiaro
    private lateinit var presenter: PopuContract.Presenter // sharedPrefs context OK

    // 1.2 inizializzo
    private val adapter = PopuAdapter()
    private val navigator = MainNavigatorK()


    // 2 METODI FRAGMENT

    // 2.1 creazione view - OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view:View? = inflater.inflate(R.layout.fragm_popu, container, false)
        binding = FragmPopuBinding.inflate(inflater, container, false) //
        return binding?.root // richiama view da binding
    }


    // 2.2 logica
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        // sharedPrefs context OK
        presenter = PopuPresenter(this, requireContext())

        with(binding!!) {

            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(requireContext(), 3)


            swipeToRefresh.setOnRefreshListener {
                presenter.getMovieAndUpdateUi(forceRefresh = true)
                // poi nascondi caricamento di swipeRefresh
                // (.isRefreshing = false) dove serve
            }
        }


        // OK - movieId lo lascio solo per chiarezza
        adapter.setCallbackVH { movieId ->
            presenter.onVHolderCLick(movieId)
        }


        // default
        presenter.getMovieAndUpdateUi(forceRefresh = false)


    }


    // CONTRACT METHOD - OK

    // OK
    override fun updateUi(list: List<PopuDto>) {
        adapter.updateList(list)
    }


    // TODO
    override fun emptyStatesFlow(emptyStates: EmptyStatesEnumNew) {
        // chiamo funzione gestione stati
        EmptyStatesManagementNew.emptyStatesFlow(
            emptyStates,
            binding?.emptyStates!!.progressBar,
            binding?.emptyStates!!.errorMsgTextview
        )

        // stop refreshing solo in questi 3 stati TODO OK
        when (emptyStates) {
            EmptyStatesEnumNew.ON_SUCCESS,
            EmptyStatesEnumNew.ON_ERROR_IO,
            EmptyStatesEnumNew.ON_ERROR_OTHER
            -> binding?.swipeToRefresh?.isRefreshing = false

            else -> {}  // non fare nulla
        }
    }


    // OK
    override fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM POPU, movieId: $movieId")
    }
}

