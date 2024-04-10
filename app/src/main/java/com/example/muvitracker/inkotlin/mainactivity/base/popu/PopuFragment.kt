package com.example.muvitracker.inkotlin.mainactivity.base.popu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.mainactivity.base.BaseAdapter
import com.example.muvitracker.inkotlin.mainactivity.base.MovieModel
import com.example.muvitracker.inkotlin.mainactivity.base.BaseContract
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement

/* kotlin
 * 1) R.layout.fragment_popular ==> .fragment_popular ==> package level propoerty
 * 2) recyclerView.layoutManager ==> .layoutManager ==> syntethic extension property
 * 3) setter -   x.setElemento(y) ==> istanza.elemento = y
 */

/**
 * caching con shared prefs
 * binding
 * empty states ridotto
 */


class PopuFragment : Fragment(), BaseContract.View {

    private lateinit var presenter: BaseContract.Presenter // sharedPrefs context OK
    private val adapter = BaseAdapter() // TODO
    private val navigator = MainNavigator()
    private var binding: FragmBaseCategoryBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view:View? = inflater.inflate(R.layout.fragm_popu, container, false)
        binding = FragmBaseCategoryBinding.inflate(inflater, container, false) //
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
            recycleView.layoutManager = GridLayoutManager(requireContext(), 2)

            swipeToRefresh.setOnRefreshListener {  // ()->Unit, interfaccia parametro vuoto
                // implementazione OnRefreshListener
                presenter.getMovieAndUpdateUi(forceRefresh = true)
                // poi nascondi caricamento di swipeRefresh
                // (.isRefreshing = false) dove serve
            }
        }

        // OK - movieId lo lascio solo per chiarezza
        adapter.setCallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
        }

        presenter.getMovieAndUpdateUi(forceRefresh = false)

    }


    // CONTRACT METHOD - OK
    override fun updateUi(list: List<MovieModel>) {
        adapter.updateList(list)
    }

    // OK
    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {
        // chiamo funzione gestione stati
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            binding?.emptyStates!!.progressBar,
            binding?.emptyStates!!.errorMsgTextview
        )

        // stop refreshing solo in questi 3 stati
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
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

