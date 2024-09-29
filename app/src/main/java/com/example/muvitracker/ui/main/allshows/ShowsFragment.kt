package com.example.muvitracker.ui.main.allshows

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
import com.example.muvitracker.ui.main.allshows.base.ShowPagingAdapter
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ShowsFragment : Fragment(R.layout.fragm_base_category) {

    val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<ShowsViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val pagingAdapter = ShowPagingAdapter(onClickVH = { showIds ->
        navigator.startShowDetailFragment(showIds)
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
            requireContext().getString(R.string.anticipated)
        )

        binding.recyclerView.adapter = pagingAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.toolbar.text = requireContext().getString(R.string.shows)

        viewModel.getMoviesFromSelectFeedCategory(selectedFeed)
        collectShowsPagingStates()

        // CHIPS
        // 1 create
        binding.chipGroupFeedCategory.removeAllViews()
        feedCategoryList.forEach { feedText ->
            val chip = Chip(context).apply {
                text = feedText
                isCheckable = true // clickable
                tag = feedText
            }
            binding.chipGroupFeedCategory.addView(chip)
        }

        // 2 click
        binding.chipGroupFeedCategory.isSingleSelection = true
        binding.chipGroupFeedCategory.isSelectionRequired = true

        binding.chipGroupFeedCategory.setOnCheckedChangeListener { chipGroup, checkedId ->
            val selectedChip = chipGroup.findViewById<Chip>(checkedId)
            selectedChip?.let { chip ->
                selectedFeed = chip.text.toString()
                viewModel.getMoviesFromSelectFeedCategory(selectedFeed) // ok todo
                collectShowsPagingStates()
                pagingAdapter.refresh()
            }
        }

        binding.chipGroupFeedCategory.post {
            val defaultChip =
                binding.chipGroupFeedCategory.findViewWithTag<Chip>(requireContext().getString(R.string.popular))
            defaultChip?.isChecked = true
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }
    }


    // ok per shows
    private fun collectShowsPagingStates() {
        fragmentViewLifecycleScope.launch {
            viewModel.statePaging.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorTextView.isVisible = false
            }
        }

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


}