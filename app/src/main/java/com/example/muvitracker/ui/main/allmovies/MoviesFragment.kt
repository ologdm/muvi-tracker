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
import com.example.muvitracker.utils.viewBinding
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragm_base_category) {

    @Inject
    lateinit var navigator: Navigator

    private val b by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<MoviesViewmodel>()

    private val pagingAdapter = MoviePagingAdapter(onClickVH = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    })



    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        b.toolbar.text = getString(R.string.movies)
        b.recyclerView.adapter = pagingAdapter
        b.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        b.chipGroupFeed.apply {
            createChipGroup()
            setupChips()
        }

        b.swipeRefreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }

        collectPagingStates()
    }

    private fun ChipGroup.createChipGroup() {
        // 1. creo i chip in base alla lista Enum
        this.removeAllViews()
        MovieType.entries.forEach { feed ->
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

    // PRIVATE METHODS
    private fun ChipGroup.setupChips() {
        // 1. inizializzo chip con feed attuale
        this.findViewWithTag<Chip>(viewModel.selectedFeed.value) // value MovieType
            ?.let { it ->
                it.isChecked = true
                b.chipsScrollView.smoothScrollTo(it.left, it.top)
                // smoothScrollTo -> scroll animato a x,y
                // (x.left, y.top) -> calcolare la posizione del chip selezionato
            }

        // 2. set chip con feed attuale
        this.setOnCheckedChangeListener { chipGroup, checkedId ->
            chipGroup.findViewById<Chip>(checkedId)?.let { chip ->
                val newFeed = chip.tag as MovieType
                viewModel.setFeed(newFeed)
//                    pagingAdapter.refresh() non serve?
            }
        }
    }


    // with paging
    private fun collectPagingStates() {
        // 1 coroutines - data
        fragmentViewLifecycleScope.launch {
            viewModel.statePaging.collect { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        // 2 coroutines - statesFlow
        fragmentViewLifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { combLoadStates ->
                // 1.
                b.progressBar.isVisible = (combLoadStates.refresh is LoadState.Loading)
                b.swipeRefreshLayout.isRefreshing = combLoadStates.refresh is LoadState.Loading


                // 2. se uno dei 3 da errore
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
            }
        }
    }

}



