package com.example.muvitracker.ui.main.allmovies

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.allmovies.base.MoviePagingAdapter
import com.example.muvitracker.ui.main.allshows.ShowsType
import com.example.muvitracker.utils.viewBinding
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragm_base_category) {

    @Inject
    lateinit var navigator: Navigator

    private val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<MoviesViewmodel>()

    private val pagingAdapter = MoviePagingAdapter(onClickVH = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.toolbar.text = getString(R.string.movies)
        binding.recyclerView.adapter = pagingAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.swipeRefreshLayout.setOnRefreshListener { pagingAdapter.refresh() }

        setupChips(MovieType.entries)

        collectSelectedFeed()
        collectPagingStates()

    }


    // PRIVATE METHODS
    private fun setupChips(feedCategoryList: List<MovieType>) {
        binding.chipGroupFeedCategory.apply {
            removeAllViews()
            // 1. create
            feedCategoryList.forEach { type ->
                val chip = Chip(context).apply {
                    text = getString(type.stringRes)
                    isCheckable = true
                    tag = type // tag type object
                }
                binding.chipGroupFeedCategory.addView(chip)
            }

            isSingleSelection = true
            isSelectionRequired = true

            val startSelectedFeed = viewModel.getLastFeed()
            val startSelectedChip =
                binding.chipGroupFeedCategory.findViewWithTag<Chip>(startSelectedFeed)
            startSelectedChip?.let {
                it.isChecked = true // check the chip
                binding.chipsScrollView.smoothScrollTo(it.left, it.top)
                // smoothScrollTo -> scroll animato a x,y
                // (x.left, y.top) -> calcolare la posizione del chip selezionato
            }

            setOnCheckedChangeListener { chipGroup, checkedId ->
                chipGroup.findViewById<Chip>(checkedId)?.let { selectedChip ->
                    val newSelectedFeed = selectedChip.tag as MovieType
                    viewModel.updateSelectedFeed(newSelectedFeed)
                    }
                }
            }
        }


    // with paging
    private fun collectPagingStates() {
        // 1 coroutines - data
        fragmentViewLifecycleScope.launch {
            viewModel.statePaging.collect { pagingData ->
                pagingAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorTextView.isVisible = false
                // basta qua, si ferma quando si ha la risposta dal pager (sia data che error)
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

    private fun collectSelectedFeed() {
        fragmentViewLifecycleScope.launch {
            viewModel.selectedFeed.collectLatest { selectedFeed ->
                viewModel.updateSelectedFeed(selectedFeed)
            }
        }
    }

}



