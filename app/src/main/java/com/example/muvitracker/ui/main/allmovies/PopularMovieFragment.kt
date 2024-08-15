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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class PopularMovieFragment : Fragment(R.layout.fragm_base_category) {

    private val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<PopularMovieViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val adapter = MoviePagingAdapter(onClickVH = { movieId ->
        navigator.startMovieDetailFragment(movieId)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        collectPagingState()// 1° coroutine
        collectPagingLoadStateFlow()// 2° coroutine necessaria

        with(binding) {
            toolbar.text = getString(R.string.popular)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

    }

    // PAGING METHODS
    private fun collectPagingState() {
        fragmentViewLifecycleScope.launch { // extended property

            viewModel.statePaging.collect { pagingData ->
                adapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorTextView.isVisible = false
                // basta qua, si spegne quando si ha la risposta dal pager (sia data che error)
            }
        }
    }


    private fun collectPagingLoadStateFlow() {
        fragmentViewLifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                // 1-OK
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                // 2 -OK
                if (loadState.refresh is LoadState.Error) {
                    binding.errorTextView.isVisible = true

                    val error = (loadState.refresh as LoadState.Error).error
                    if (error is IOException) {
                        binding.errorTextView.text =
                            requireContext().getString(R.string.error_message_no_internet)
                    } else {
                        binding.errorTextView.text =
                            requireContext().getString(R.string.error_message_other)
                    }
                }
            }
        }
    }


}


