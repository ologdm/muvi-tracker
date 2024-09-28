package com.example.muvitracker.ui.main.allmovies

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryNewBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.allmovies.base.MovieAdapter
import com.example.muvitracker.ui.main.allmovies.base.MoviePagingAdapter
import com.example.muvitracker.utils.viewBinding
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.example.muvitracker.utils.statesFlow
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragm_base_category_new) {

    private val binding by viewBinding(FragmBaseCategoryNewBinding::bind)
    private val viewModel by viewModels<MoviesViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val pagingAdapter = MoviePagingAdapter(onClickVH = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    })
    private val adapter = MovieAdapter(onClickVH = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    })


    private var selectedFeed = "" // default, last from bundle


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        val feedCategoryList = listOf(
            requireContext().getString(R.string.popular),
            requireContext().getString(R.string.watched),
            requireContext().getString(R.string.favorited),
            requireContext().getString(R.string.anticipated),
            requireContext().getString(R.string.box_office),
        )

        // 2 adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.toolbar.text = requireContext().getString(R.string.movies)

        // default call
        viewModel.getMoviesFromSelectFeedCategory(selectedFeed)
        collectMoviesPagingStates()


        // CHIPS
        // 1.1 create feed chips from defined list - ok
        // create
        binding.chipGroupFeedCategory.removeAllViews()
        feedCategoryList.forEach { feedText ->
            val chip = Chip(context).apply {
                text = feedText
                isCheckable = true
                tag = feedText // set a tag to use it for default selection todo
            }
            binding.chipGroupFeedCategory.addView(chip)
        }

        // select default chip (todo modificare da bundle)
        binding.chipGroupFeedCategory.post {
            val defaultChip =
                binding.chipGroupFeedCategory.findViewWithTag<Chip>(requireContext().getString(R.string.popular))
            defaultChip?.isChecked = true
        }

        // click on selected chips
        binding.chipGroupFeedCategory.isSingleSelection = true
        binding.chipGroupFeedCategory.isSelectionRequired = true

        binding.chipGroupFeedCategory.setOnCheckedChangeListener { chipGroup, checkedId ->
            val selectedChip = chipGroup.findViewById<Chip>(checkedId)
            selectedChip?.let { chip ->
                selectedFeed = chip.text.toString() // selectedFeed come chips clickato
                if (selectedFeed == requireContext().getString(R.string.box_office)) {
                    binding.recyclerView.adapter = adapter // switch to boxoffice adapter todo
                    viewModel.loadBoxoMovies()
                    collectBoxofficeState()
                } else {
                    binding.recyclerView.adapter = pagingAdapter // switch back to paging adapter
                    viewModel.getMoviesFromSelectFeedCategory(selectedFeed) // ok todo
                    collectMoviesPagingStates()
                    pagingAdapter.refresh()
                }
            }
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            if (selectedFeed == requireContext().getString(R.string.box_office)) {
                adapter.submitList(emptyList()) // pulisci la lista per ricaricare
                viewModel.loadBoxoMovies() // carica i film del boxoffice
            } else {
                pagingAdapter.refresh() // refresh per paging
            }

        }

    }


    // PRIVATE METHODS
    private fun collectMoviesPagingStates() {
        // 1 coroutines - data
        fragmentViewLifecycleScope.launch { // extended property
            viewModel.statePaging.collect { pagingData ->
                pagingAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorTextView.isVisible = false
                // basta qua, si spegne quando si ha la risposta dal pager (sia data che error)
            }
        }

        // 2 coroutines - statesFlow
        fragmentViewLifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadState ->
                // 1 progress
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                // 2 error
                if (loadState.refresh is LoadState.Error) {
                    binding.errorTextView.isVisible = true

                    val error = (loadState.refresh as LoadState.Error).error
                    binding.errorTextView.text = if (error is IOException) {
                        requireContext().getString(R.string.error_message_no_internet)
                    } else {
                        requireContext().getString(R.string.error_message_other)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }


    private fun collectBoxofficeState() {
        // 2 observe boxoffice
        viewModel.boxoState.observe(viewLifecycleOwner) { stateContainer ->
            adapter.submitList(stateContainer.data)
            binding.swipeRefreshLayout.isRefreshing = false
            stateContainer.statesFlow(
                errorTextview = binding.errorTextView,
                progressBar = binding.progressBar
            )
        }
    }

}



