package com.example.muvitracker.ui.main.allmovies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.allmovies.base.MovieAdapter
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BoxoMovieFragment : Fragment(R.layout.fragm_base_category) {

    private val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<BoxoMovieViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val adapter = MovieAdapter(onClickVH = { movieId ->
        navigator.startMovieDetailFragment(movieId)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        viewModel.state.observe(viewLifecycleOwner) { stateContainer ->
            adapter.submitList(stateContainer.data)

            stateContainer.statesFlow(
                errorTextview = binding.errorTextView,
                progressBar = binding.progressBar
            )
        }

        with(binding) {
            toolbar.text = getString(R.string.box_office)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadMovies()
            binding.swipeRefreshLayout.isRefreshing = false // TODO  - manage all the states, stop
        }
    }

}

