package com.example.muvitracker.inkotlin.ui.main.boxo

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
import com.example.muvitracker.inkotlin.ui.main.MainNavigator
import com.example.muvitracker.inkotlin.ui.main.base.MovieAdapter
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum
import com.example.muvitracker.inkotlin.utils.EmptyStatesManagement


class BoxoFragmentVM : Fragment() {

    private val adapter = MovieAdapter()
    private val navigator = MainNavigator()
    private var binding: FragmBaseCategoryBinding? = null

    // !!!! viewModel - creazione automatica
    private val viewModel by viewModels<BoxoViewModel>()


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

        // aggiorno ogni volta che cambia stato su VModel
        viewModel.boxoList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })

        viewModel.emptyState.observe(viewLifecycleOwner, Observer {
            handleEmptyStates(it)
        })



        with(binding!!) {

            toolbar.text = getString(R.string.box_office)

            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(requireContext(), 2)

            swipeToRefresh.setOnRefreshListener {
                viewModel.loadMovies(isRefresh = true)
            }
        }

        adapter.setCallbackVH { movieId ->
            startDetailsFragment(movieId)
        }

        viewModel.loadMovies(isRefresh = false)
    }


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

            else -> {}
        }
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM BOXO, movieId: $movieId")
    }

}