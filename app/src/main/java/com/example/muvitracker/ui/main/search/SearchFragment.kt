package com.example.muvitracker.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmentSearchBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.search.adapters.SearchResultsAdapter
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by viewModels<SearchViewModel>()

    @Inject
    lateinit var navigator: Navigator
    private val adapter = SearchResultsAdapter(
        onClickVHMovie = { movieIds ->
            navigator.startMovieDetailFragment(movieIds)
        },
        onClickVHShow = { showIds ->
            navigator.startShowDetailFragment(showIds)
        },
        onClickVHPerson = { personIds ->
            navigator.startPersonFragmentFromSearch(personIds)
        })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // 1) get search data
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        fragmentViewLifecycleScope.launch {
            viewModel.statePaging.collectLatest { data ->
                adapter.submitData(data)
            }

        }


        // 2) set search parameters
        binding.searchEditText.doAfterTextChanged { s ->
            viewModel.updateQuery(s.toString())
        }

        binding.searchChipGroup.isSingleSelection = true
        binding.searchChipGroup.isSelectionRequired = true
        binding.searchChipGroup.check(R.id.chipAll) // default selected chip
        binding.searchChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val filterValue = when (checkedId) {
                R.id.chipAll -> MOVIE_SHOW_PERSON
                R.id.chipMovies -> MOVIE
                R.id.chipShows -> SHOW
                R.id.chipPeople -> PERSON
                else -> MOVIE_SHOW_PERSON
            }
            viewModel.updateFilterValue(filterValue)
        }

        mainLayoutTopEdgeToEdgeManagment()
    }

    private fun mainLayoutTopEdgeToEdgeManagment() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // aggiorno solo lati che mi servono
            v.updatePadding(top = systemBars.top)
            insets
        }
    }


    companion object {
        const val MOVIE_SHOW_PERSON = "movie,show,person"
        private const val MOVIE = "movie"
        private const val SHOW = "show"
        private const val PERSON = "person"
    }
}

