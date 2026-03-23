package com.example.presentation.prefs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.Navigator
import com.example.presentation.R
import com.example.presentation.databinding.FragmentPrefsSonBinding
import com.example.presentation.prefs.adapters.PrefsShowsAdapter
import com.example.presentation.utils.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrefsShowsFragment : Fragment(R.layout.fragment_prefs_son) {

    private val viewModel by viewModels<PrefsShowsViewModel>()
    private val binding by viewBinding(FragmentPrefsSonBinding::bind)

    @Inject
    lateinit var navigator: Navigator

    private val adapter = PrefsShowsAdapter(
        onClickVH = { showIds ->
            navigator.startShowDetailFragment(showIds)
        },
        onLongClickVH = { showId ->
            startDeleteAlertDialog(showId, getString(R.string.discard_show))
        },
        onCLickLiked = { showId ->
            viewModel.toggleLikedShow(showId)
        },
        onClickWatchedAllCheckbox = { showIds, adapterCallback ->
            viewModel.updateWatchedAllSingleShow(showIds, onComplete = {
                adapterCallback() // esegui operazione su adapter
            })
        },
        onNotLikedNotWatched = { show ->
            startDeleteAlertDialog(
                show.ids.trakt,
                getString(R.string.is_no_longer_liked_or_watched_remove_it, show.title)
            )
        }
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.prefsList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }


    // ---------------------------------------------------------------------------------------------------
    private fun startDeleteAlertDialog(showId: Int, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                viewModel.deleteItem(showId)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}

