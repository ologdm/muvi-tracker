package com.example.muvitracker.inkotlin.ui.mainactivity.popu.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.inkotlin.ui.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.ui.mainactivity.base.MovieAdapter
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum
import com.example.muvitracker.inkotlin.utils.EmptyStatesManagement


class PopuFragmentVM : Fragment() {

    private val adapter = MovieAdapter()
    private val navigator = MainNavigator()
    private var binding: FragmBaseCategoryBinding? = null

    // !!!! viewModel - creazione automatica
    private val viewModel by viewModels<PopuViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmBaseCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        // boxoViewModel - si inizializza da solo

        // Vmodel OK
        viewModel.popuList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })

        viewModel.emptyState.observe(viewLifecycleOwner, Observer {
            handleEmptyStates(it)
        })


        with(binding!!) {

            toolbar.text = getString(R.string.popular)
            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(requireContext(), 2)

            viewModel.loadMovies(isRefresh = false)

            swipeToRefresh.setOnRefreshListener {
                // presenter.getMovieAndUpdateUi(forceRefresh = true)
                viewModel.loadMovies(isRefresh = true)
            }
        }

        adapter.setCallbackVH { movieId ->
            // presenter.onVHolderClick(movieId)
            // senza passaggio intermedio da presenter
            startDetailsFragment(movieId)
        }

    }


    // FUNZIONI CONTRACT -> PRIVATE
    // non sono piu da chiamare da presenter,
    // ma direttamente da view
    // con observer che aggiorna solamente stato loro parametri

    // fun updateUi(popuList) - non piu necessaria


    private fun handleEmptyStates(emptyStates: EmptyStatesEnum) {
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            binding!!.progressBar,
            binding!!.errorMsgTextview
        )
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> binding?.swipeToRefresh?.isRefreshing = false

            else -> {}  // non fare nulla
        }
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM POPU, movieId: $movieId")
    }
}



