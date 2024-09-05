package com.example.muvitracker.ui.main.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.FragmSearchBinding
import com.example.muvitracker.ui.main.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// X - modifiche da chipgroup filter

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmSearchBinding? = null
    private val binding
        get() = _binding

    private val viewModel by viewModels<SearchViewModel>()

    @Inject
    lateinit var navigator: Navigator
    private val adapter = SearchAdapter(onClickVHMovie = { movieId ->
        startMovieDetailFragment(movieId)
    },
        onClickVHShow = { showIds -> // X
            startDetailShowFragment(showIds)
        })

    // Debouncing
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val DEBOUNCE_DELAY: Long = 300L


    private var filterValue: String = "movie,show" // X
    private var currentSearchText: String = "" // X


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // TODO
        //   adapter OK
        //   chip select OK
        //   funzione chiamata dati OK


        viewModel.searchState.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        with(binding!!) {
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
        binding?.searchChipGroup?.isSingleSelection = true // selezione uno alla volta
        binding?.searchChipGroup?.check(R.id.chipAll) // default
        binding?.searchChipGroup?.setOnCheckedChangeListener { chipGroup, checkedId ->
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


    private fun startMovieDetailFragment(movieId: Int) {
        navigator.startMovieDetailFragment(movieId)
    }


    private fun startDetailShowFragment(showId: Ids) { // X
        navigator.startShowDetailFragment(showId)
    }

}

//override fun onDestroyView() {
//    super.onDestroyView()
//    _binding = null
//}
