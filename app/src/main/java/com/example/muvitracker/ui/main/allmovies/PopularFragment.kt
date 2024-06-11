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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularFragment : Fragment() {

    private var _binding: FragmBaseCategoryBinding? = null
    private val binding
        get() = _binding

    private val navigator = Navigator()

    // senza inject
    private val viewModel by viewModels<PopularViewModel>()

    private val adapter = MovieAdapter(onClickVH = { movieId ->
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
        viewModel.getMovies().observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.data)
            state.statesFlow(
                progressBar = binding!!.progressBar,
                errorMsg = binding!!.errorTextView,
            )
            println("XXX OBSERVING STATE: $state")
        }

        with(binding!!) {
            toolbar.text = getString(R.string.popular)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(requireActivity(), movieId)
    }

}



