package com.example.muvitracker.ui.main.allshows

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmentExploreBaseBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.allshows.base.ShowPagingAdapter
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class ShowsFragment : Fragment(R.layout.fragment_explore_base) {

    @Inject
    lateinit var navigator: Navigator

    val b by viewBinding(FragmentExploreBaseBinding::bind)
    private val viewModel by viewModels<ShowsViewmodel>()

    private val pagingAdapter = ShowPagingAdapter(onClickVH = { showIds ->
        navigator.startShowDetailFragment(showIds)
    })

    var shouldScrollToTop = false


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        b.toolbar.text = getString(R.string.shows)
        b.recyclerView.adapter = pagingAdapter
        b.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        b.chipGroupFeed.apply {
            createChipGroup()
            setupChips()
        }


        b.swipeRefreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }

        collectPagingStates() // observe paging data and loadStateFlow
    }


    // PRIVATE FUNCTIONS
    private fun ChipGroup.createChipGroup() {
        // 1. creo i chip in base alla lista Enum
        this.removeAllViews()
        ShowsType.entries.forEach { feed ->
            val chip = Chip(context).apply {
                text = getString(feed.stringRes)
                isCheckable = true
                tag = feed // tag type object
            }
            addView(chip)
        }
        this.isSingleSelection = true
        this.isSelectionRequired = true

    }


    private fun ChipGroup.setupChips() {
        // 1. inizializzo chip con feed attuale
        this.findViewWithTag<Chip>(viewModel.selectedFeed.value)
            ?.let {
                it.isChecked = true // check the chip
                b.chipsScrollView.smoothScrollTo(it.left, it.top)
            }

        // 2. set chip con feed attuale
        this.setOnCheckedChangeListener { chipGroup, checkedId ->
            chipGroup.findViewById<Chip>(checkedId)?.let { chip ->
                val newFeed = chip.tag as ShowsType
                viewModel.setFeed(newFeed)
                shouldScrollToTop = true
            }
        }
    }


    private fun collectPagingStates() {
        fragmentViewLifecycleScope.launch {
            viewModel.statePaging.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        fragmentViewLifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { combLoadStates ->
                // 1. loading
                b.progressBar.isVisible = combLoadStates.refresh is LoadState.Loading
                b.swipeRefreshLayout.isRefreshing = combLoadStates.refresh is LoadState.Loading

                // 2 error
                val errorState =
                    listOf(combLoadStates.refresh, combLoadStates.prepend, combLoadStates.append)
                        .filterIsInstance<LoadState.Error>()
                        .firstOrNull()


                b.errorTextView.isVisible = (errorState != null)
                b.errorTextView.text = if (errorState?.error is IOException) {
                    requireContext().getString(R.string.error_message_no_internet_swipe_down)
                } else {
                    requireContext().getString(R.string.error_message_other)
                }

                // fix 1.1.2 - paging does not invalidate when filter changes
                if (combLoadStates.refresh is LoadState.NotLoading && shouldScrollToTop) {
                    b.recyclerView.scrollToPosition(0)
                    shouldScrollToTop = false
                }
            }

        }
    }
}

