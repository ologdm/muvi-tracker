package com.example.muvitracker.ui.main.allmovies

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.allmovies.base.MoviePagingAdapter
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PopularFragment : Fragment(R.layout.fragm_base_category) {

    private val binding by viewBinding(FragmBaseCategoryBinding::bind)
    private val viewModel by viewModels<PopularViewModel>()

    @Inject
    lateinit var navigator: Navigator

    private val adapter = MoviePagingAdapter(onClickVH = { movieId ->
        startDetailsFragment(movieId)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // 1° coroutine - // TODO farla come extension function
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.statePaging.collect {
                adapter.submitData(it)
            }
        }

        // 2° coroutine necessaria
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
//                binding.retry.isVisible = loadState.refresh !is LoadState.Loading
                binding.errorTextView.isVisible = loadState.refresh is LoadState.Error
            }
        }

        with(binding) {
            toolbar.text = getString(R.string.popular)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(
            movieId
        )
        println("XXX_POPULAR_movieId: $movieId") // OK arriva
    }

}



