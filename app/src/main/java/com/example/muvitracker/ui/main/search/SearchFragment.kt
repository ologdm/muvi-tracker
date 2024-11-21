package com.example.muvitracker.ui.main.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmSearchBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragm_search) {

    private val binding by viewBinding (FragmSearchBinding::bind)
    private val viewModel by viewModels<SearchViewModel>()

    @Inject
    lateinit var navigator: Navigator
    private val adapter = SearchAdapter(onClickVHMovie = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    },
        onClickVHShow = { showIds ->
            navigator.startShowDetailFragment(showIds)
        })

    // Debouncing
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val DEBOUNCE_DELAY: Long = 300L


    private var filterValue: String = "movie,show" // default X
    private var currentSearchText: String = "" // default X



    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        viewModel.searchState.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        with(binding) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.adapter = adapter

            searchEditText.doAfterTextChanged { s ->  // kotlin abbreviated method
                currentSearchText = s.toString() // X

                searchRunnable
                    ?.let {// 1. cancel the previous runnable to implement debouncing
                        handler.removeCallbacks(it)
                    }
                searchRunnable =
                    Runnable { // 2. defines a new runnable that will perform the search
                        viewModel.updateSearch(filterValue, currentSearchText) // X
                    }
                searchRunnable
                    ?.let {// 3. schedule the new runnable with a specified delay to perform the debouncing
                        handler.postDelayed(it, DEBOUNCE_DELAY)
                    }
            }
        }


        // chip group // X
        binding.searchChipGroup.isSingleSelection = true // selezione uno alla volta
        binding.searchChipGroup.isSelectionRequired = true // selezione uno alla volta
        binding.searchChipGroup.check(R.id.chipAll) // default
        binding.searchChipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
            filterValue = when (checkedId) {
                R.id.chipAll -> "movie,show"
                R.id.chipMovie -> "movie"
                R.id.chipShow -> "show"
//                R.id.chipPeople -> "people" TODO
                else -> "movie, show"
            }
            // Chiama ricerca con nuovo filtro
            viewModel.updateSearch(filterValue, currentSearchText)
        }
    }


}

