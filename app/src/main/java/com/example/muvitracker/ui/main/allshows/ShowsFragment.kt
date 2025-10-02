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

    @Inject
    lateinit var navigator: Navigator

    val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<ShowsViewmodel>()

    private val pagingAdapter = ShowPagingAdapter(onClickVH = { showIds ->
        navigator.startShowDetailFragment(showIds)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.toolbar.text = getString(R.string.shows)
        binding.recyclerView.adapter = pagingAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        setupChips(ShowsType.entries) // entries rispetta ordine
        binding.swipeRefreshLayout.setOnRefreshListener { pagingAdapter.refresh() }

        collectSelectedFeed() // observe selected feed
        collectPagingStates() // observe paging data and loadStateFlow
    }


    // MY FUNCTIONS
    private fun setupChips(feedCategoryList: List<ShowsType>) {
        binding.chipGroupFeed.apply {
            removeAllViews()
            feedCategoryList.forEach { type ->
                val chip = Chip(context).apply {
                    text = getString(type.stringRes)
                    isCheckable = true
                    tag = type
                }
                binding.chipGroupFeed.addView(chip)
            }
            isSingleSelection = true
            isSelectionRequired = true

            val startSelectedFeed = viewModel.getLastFeed()
            val startSelectedChip =
                binding.chipGroupFeed.findViewWithTag<Chip>(startSelectedFeed)
            startSelectedChip?.let {
                it.isChecked = true // check the chip
                binding.chipsScrollView.smoothScrollTo(it.left, it.top)
            }

            setOnCheckedChangeListener { chipGroup, checkedId ->
                chipGroup.findViewById<Chip>(checkedId)?.let { selectedChip ->
                    val newSelectedFeed = selectedChip.tag as ShowsType // tag lo forza a cast
                    viewModel.updateSelectedFeed(newSelectedFeed)
                }
            }
        }
    }


    private fun collectPagingStates() {
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
                        requireContext().getString(R.string.error_message_no_internet_swipe_down)
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