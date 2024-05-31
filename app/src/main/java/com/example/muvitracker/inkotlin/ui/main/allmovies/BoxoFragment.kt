package com.example.muvitracker.inkotlin.ui.main.allmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.inkotlin.ui.main.Navigator
import com.example.muvitracker.inkotlin.ui.main.allmovies.base.MovieAdapter
import com.example.muvitracker.inkotlin.utils.statesFlow
import com.google.android.material.progressindicator.LinearProgressIndicator


class BoxoFragment : Fragment() {

    private val adapter = MovieAdapter()
    private val navigator = Navigator()
    private var binding: FragmBaseCategoryBinding? = null


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

        viewModel.stateContainer.observe(viewLifecycleOwner) {
            adapter.updateList(it.dataList)

            it.statesFlow(
                progressBar = binding!!.progressBar,
                errorMsg = binding!!.errorMsgTextview,
                null
            )
        }

        with(binding!!) {
            toolbar.text = getString(R.string.box_office)
            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(requireContext(), 2)

            swipeToRefresh.setOnRefreshListener {
                viewModel.loadMovies(isRefresh = true) // refresdh
            }
        }

        adapter.setCallbackVH { movieId ->
            startDetailsFragment(movieId)
        }
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
//        println("XXX_ START FRAGMENT FROM BOXO, movieId: $movieId")
    }

}




//    private fun handleEmptyStates(emptyStates: EmptyStatesEnum) {
//        EmptyStatesManagement.emptyStatesFlow(
//            emptyStates,
//            binding!!.progressBar,
//            binding!!.errorMsgTextview
//        )
//        when (emptyStates) {
//            EmptyStatesEnum.ON_SUCCESS,
//            EmptyStatesEnum.ON_ERROR_IO,
//            EmptyStatesEnum.ON_ERROR_OTHER
//            -> binding?.swipeToRefresh?.isRefreshing = false
//
//            else -> {}
//        }
//    }