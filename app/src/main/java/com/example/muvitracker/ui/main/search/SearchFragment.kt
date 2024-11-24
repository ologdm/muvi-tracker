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
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        onClickVHPerson = { personIds ->
            navigator.startPersonFragmentFromSearch(personIds)
        })

    // TODO: eliminare
    // Debouncing
//    private val handler = Handler(Looper.getMainLooper())
//    private var searchRunnable: Runnable? = null
//    private val DEBOUNCE_DELAY: Long = 300L


    // default values
//    private var filterValue: String = MOVIE_SHOW_PERSON // todo default su viewmodel
//    private var currentSearchText: String = ""


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        // TODO: new OK
        binding.searchEditText.doAfterTextChanged { s ->
            // 3) cambia valore stringa + filter
            viewModel.updateQuery(s.toString())
            // chiamata risultati -> su viewmodel 4)

        }


        // chip group OK
        binding.searchChipGroup.isSingleSelection = true // selezione uno alla volta
        binding.searchChipGroup.isSelectionRequired = true // selezione uno alla volta
        binding.searchChipGroup.check(R.id.chipAll) // selezione chip default
        binding.searchChipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
            val filterValue = when (checkedId) {
                R.id.chipAll -> MOVIE_SHOW_PERSON
                R.id.chipMovies -> MOVIE
                R.id.chipShows -> SHOW
                R.id.chipPeople -> PERSON
                else -> MOVIE_SHOW_PERSON
            }

            // 4 update filter value
            viewModel.updateFilterValue(filterValue)
        }


        // update list TODO old, elimina
//        viewModel.searchResultState.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }
        // TODO: da livedata a stateFlow
        fragmentViewLifecycleScope.launch {
            viewModel.searchResultState.collectLatest {listSearchResult ->
                adapter.submitList(listSearchResult)
                println(listSearchResult.toString())
            }
        }




        // TODO paging3
    }


    companion object {
        const val MOVIE_SHOW_PERSON = "movie,show,person"
        private const val MOVIE = "movie"
        private const val SHOW = "show"
        private const val PERSON = "person"
    }


}

