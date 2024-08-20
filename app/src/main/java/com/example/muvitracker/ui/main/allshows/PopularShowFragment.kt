package com.example.muvitracker.ui.main.allshows

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
import com.example.muvitracker.ui.main.allshows.base.ShowPagingAdapter
import com.example.muvitracker.utils.fragmentViewLifecycleScope
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class PopularShowFragment : Fragment(R.layout.fragm_base_category_new) {

    val binding by viewBinding(FragmBaseCategoryNewBinding::bind)
    private val viewmodel by viewModels<PopularShowViewmodel>()
    @Inject
    lateinit var navigator: Navigator

    private val adapter = ShowPagingAdapter(onClickVH = { showIds ->
        navigator.startShowDetailFragment(showIds)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        fragmentViewLifecycleScope.launch {
            viewmodel.statePaging.collectLatest {
                adapter.submitData(it)
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorTextView.isVisible = false
            }
        }

        fragmentViewLifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {loadState->
                // 1
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                // 2
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

        binding.toolbar.text = "Popular Shows"
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }


}