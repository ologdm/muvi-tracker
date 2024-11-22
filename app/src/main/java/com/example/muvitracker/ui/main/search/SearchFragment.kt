package com.example.muvitracker.ui.main.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
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

    private val binding by viewBinding(FragmSearchBinding::bind)
    private val viewModel by viewModels<SearchViewModel>()

    @Inject
    lateinit var navigator: Navigator
    private val adapter = SearchAdapter(
        onClickVHMovie = { movieIds ->
            navigator.startMovieDetailFragment(movieIds)
        },
        onClickVHShow = { showIds ->
            navigator.startShowDetailFragment(showIds)
        },
        onClickVHPerson = {personIds->
            Toast.makeText(requireContext(),"click su person", Toast.LENGTH_SHORT).show()
        })

    // Debouncing
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val DEBOUNCE_DELAY: Long = 300L

    // default values
    private var filterValue: String = MOVIE_SHOW_PERSON
    private var currentSearchText: String = ""


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


        // chip group
        binding.searchChipGroup.isSingleSelection = true // selezione uno alla volta
        binding.searchChipGroup.isSelectionRequired = true // selezione uno alla volta
        binding.searchChipGroup.check(R.id.chipAll) // default
        binding.searchChipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
            filterValue = when (checkedId) {
                R.id.chipAll -> MOVIE_SHOW_PERSON
                R.id.chipMovies -> MOVIE
                R.id.chipShows -> SHOW
                R.id.chipPeople -> PERSON
                else -> MOVIE_SHOW_PERSON
            }
            // Chiama ricerca con nuovo filtro
            viewModel.updateSearch(filterValue, currentSearchText)
        }
    }


    companion object {
        private const val MOVIE_SHOW_PERSON = "movie,show,person"
        private const val MOVIE = "movie"
        private const val SHOW = "show"
        private const val PERSON = "person"
    }


}

