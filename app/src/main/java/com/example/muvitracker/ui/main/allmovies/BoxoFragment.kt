package com.example.muvitracker.ui.main.allmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.allmovies.base.MovieAdapter
import com.example.muvitracker.utils.statesFlow


class BoxoFragment : Fragment() {

    private var _binding: FragmBaseCategoryBinding? = null
    private val binding
        get() = _binding

    private val viewModel by viewModels<BoxoViewModel>()
    private val navigator = Navigator()

    private val adapter = MovieAdapter(onClickVH = { movieId->
        startDetailsFragment(movieId)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmBaseCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        viewModel.stateContainer.observe(viewLifecycleOwner) {state->
            adapter.submitList(state.dataList)

            state.statesFlow(
                progressBar = binding!!.progressBar,
                errorMsg = binding!!.errorTextView,
                null
            )
        }

        with(binding!!) {
            toolbar.text = getString(R.string.box_office)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

            swipeToRefresh.setOnRefreshListener {
                viewModel.loadMovies(isRefresh = true)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(
            requireActivity(),
            movieId
        )
//        println("XXX_ START FRAGMENT FROM BOXO, movieId: $movieId")
    }

}

