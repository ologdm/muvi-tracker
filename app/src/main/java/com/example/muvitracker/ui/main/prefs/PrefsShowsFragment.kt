package com.example.muvitracker.ui.main.prefs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmentPrefsSonBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.prefs.adapters.PrefsShowsAdapter
import com.example.muvitracker.utils.viewBinding
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
            startDeleteAlertDialog(showId, "Discard movie")
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
                "\"${show.title}\" isn’t liked or watched yet. \n \n Remove it?"
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


    // #######################################################################################
    private fun startDeleteAlertDialog(showId: Int, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteItem(showId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}

