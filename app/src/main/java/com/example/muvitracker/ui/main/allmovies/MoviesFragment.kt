package com.example.muvitracker.ui.main.allmovies

import android.content.SharedPreferences
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
class MoviesFragment : Fragment(R.layout.fragm_base_category) {

    companion object {
        private const val SELECTED_FEED_KEY = "show_selected_feed_key"
    }

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    @Inject
    lateinit var navigator: Navigator

    private val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<MoviesViewmodel>()

    private val pagingAdapter = MoviePagingAdapter(onClickVH = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    })
    private val adapter = MovieAdapter(onClickVH = { movieIds ->
        navigator.startMovieDetailFragment(movieIds)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val feedCategoryList = listOf(
            requireContext().getString(R.string.popular),
            requireContext().getString(R.string.box_office),
            requireContext().getString(R.string.watched),
            requireContext().getString(R.string.favorited),
            requireContext().getString(R.string.anticipated),
        )

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.toolbar.text = requireContext().getString(R.string.movies)

        setupChipsAndObserveTheRightCategory(feedCategoryList)
        setupSwipeRefresh()

        // observe feed changes ->  and observe statePaging or boxoState
        collectSelectedFeed()

    }


    // PRIVATE METHODS
    private fun setupChipsAndObserveTheRightCategory(feedCategoryList: List<String>) {
        binding.chipGroupFeedCategory.apply {
            removeAllViews()
            // 1. create
            feedCategoryList.forEach { feedText ->
                val chip = Chip(context).apply {
                    text = feedText
                    isCheckable = true
                    tag = feedText
                }
                binding.chipGroupFeedCategory.addView(chip)
            }

            // 2. post execute
            // ...posticipa l'esecuzione {..}  fino a quando la view 'chipGroup' non e stata inizializzata
            post {
                // load last selected item or default
                val startSelectedFeed =
                    sharedPrefs.getString(SELECTED_FEED_KEY, getString(R.string.popular))
                val startSelectedChip = binding.chipGroupFeedCategory
                    .findViewWithTag<Chip>(startSelectedFeed)// find the chip

                startSelectedChip?.let {
                    it.isChecked = true // check the chip
                    binding.chipsScrollView.smoothScrollTo(it.left, it.top)
                    // smoothScrollTo -> scroll animato a x,y
                    // (x.left, y.top) -> calcolare la posizione del chip selezionato
                }
            }

            // 3. configutazions
            isSingleSelection = true
            isSelectionRequired = true

            // 4. click on selected chips
            setOnCheckedChangeListener { chipGroup, checkedId ->
                chipGroup.findViewById<Chip>(checkedId)?.let { selectedChip ->
                    val newSelectedFeed = selectedChip.text.toString()
                    viewModel.updateSelectedFeed(newSelectedFeed)
                    sharedPrefs.edit().putString(SELECTED_FEED_KEY, newSelectedFeed).apply()
                    // switch RecyclerView Adapter, and reload data
                    if (newSelectedFeed == getString(R.string.box_office)) {
                        binding.recyclerView.adapter = adapter
                        viewModel.loadBoxoMovies()
                        collectBoxofficeList()
                    } else {
                        binding.recyclerView.adapter = pagingAdapter
                        collectPagingStates()
                        pagingAdapter.refresh()
                    }
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.selectedFeed.value.let { feed ->
                if (feed == getString(R.string.box_office)) {
                    adapter.submitList(emptyList())
                    viewModel.loadBoxoMovies()
                } else {
                    pagingAdapter.refresh()
                }
            }

        }
    }


    // with pagin
    private fun collectPagingStates() {
        // 1 coroutines - data
        fragmentViewLifecycleScope.launch { // extended property
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

    // no paging, cached list
    private fun collectBoxofficeList() {
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

    private fun collectSelectedFeed() {
        fragmentViewLifecycleScope.launch {
            viewModel.selectedFeed.collectLatest { selectedFeed ->
                viewModel.updateSelectedFeed(selectedFeed)
            }
        }
    }

}



